package ServidorApresentacao.Action.masterDegree.administrativeOffice.marksManagement;

import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import DataBeans.InfoEnrolment;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorAplicacao.Servico.exceptions.NotAuthorizedException;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import ServidorApresentacao.Action.sop.utils.SessionUtils;
import Util.TipoCurso;
import framework.factory.ServiceManagerServiceFactory;

/*
 * 
 * @author Fernanda Quit�rio 17/Fev/2004
 *  
 */
public class MarksManagementDispatchAction extends DispatchAction
{
	public ActionForward prepareChooseMasterDegree(
			ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response)
	throws Exception
	{
		ActionErrors errors = new ActionErrors();
		
		List masterDegrees = null;
		IUserView userView = SessionUtils.getUserView(request);
		TipoCurso degreeType = TipoCurso.MESTRADO_OBJ;
		Object args[] = { degreeType };
		try
		{
			masterDegrees = (List) ServiceManagerServiceFactory.executeService(userView, "ReadAllMasterDegrees", args);
		} catch (NonExistingServiceException e)
		{
			errors.add("noMasterDegree", new ActionError("error.masterDegree.noDegrees"));
			saveErrors(request,errors);
			return mapping.getInputForward();
		}

		request.setAttribute(SessionConstants.MASTER_DEGREE_LIST, masterDegrees);
		
		return mapping.findForward("showMasterDegrees");
	}
	

	public ActionForward prepareChooseDegreeCurricularPlan(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception
	{
		ActionErrors errors = new ActionErrors();
		
		String masterDegreeId  = getFromRequest("degreeId", request);
		
		List degreeCurricularPlans = null;
		IUserView userView = SessionUtils.getUserView(request);
		Object args[] = { Integer.valueOf(masterDegreeId) };
		try
		{

			degreeCurricularPlans =
			(List) ServiceManagerServiceFactory.executeService(userView, "ReadCPlanFromChosenMasterDegree", args);

		} catch (NonExistingServiceException e)
		{
			errors.add("noDegreeCurricularPlan", new ActionError("error.masterDegree.noDegreeCurricularPlan"));
			saveErrors(request,errors);
			return prepareChooseMasterDegree(mapping, form, request,response);
		}

		Collections.sort(degreeCurricularPlans,new BeanComparator("name"));
		
		request.setAttribute("degreeCurricularPlans", degreeCurricularPlans);
		
		return mapping.findForward("showDegreeCurricularPlans");
	}

	public ActionForward chooseCurricularCourses(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception
	{
		
		String degreeCurricularPlanId = getFromRequest("objectCode", request);
		getFromRequest("degreeId", request);
		
		Object args[] = { Integer.valueOf(degreeCurricularPlanId) };
		IUserView userView = SessionUtils.getUserView(request);
		List curricularCourseList = null;
		try
		{
			curricularCourseList =
				(List) ServiceManagerServiceFactory.executeService(
					userView,
					"ReadCurricularCoursesByDegreeCurricularPlanId",
					args);
		}
		catch (FenixServiceException e)
		{
			throw new FenixActionException(e);
		}
		
		Collections.sort(curricularCourseList, new BeanComparator("name"));
		request.setAttribute("curricularCourses", curricularCourseList);

		return mapping.findForward("showCurricularCourses");
	}
	
	public ActionForward getStudentMarksList(
			ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response)
	throws Exception
	{
		String curricularCourseId = getFromRequest("courseId", request);
		getFromRequest("objectCode", request);
		getFromRequest("degreeId", request);
		
		List listEnrolmentEvaluation = null;
		IUserView userView = SessionUtils.getUserView(request);
		Object args[] = { userView, Integer.valueOf(curricularCourseId), null };
		try
		{
			listEnrolmentEvaluation =
			(List) ServiceManagerServiceFactory.executeService(
					userView,
					"ReadStudentMarksListByCurricularCourse",
					args);
		} catch (NotAuthorizedException e)
		{
			return mapping.findForward("NotAuthorized");
		} catch (NonExistingServiceException e)
		{
			ActionErrors errors = new ActionErrors();
			errors.add("nonExisting", new ActionError("error.exception.noStudents"));
			saveErrors(request, errors);
			return chooseCurricularCourses(mapping, form,request,response);
		}catch (FenixServiceException e)
		{
			throw new FenixActionException(e);
		}

		if (listEnrolmentEvaluation.size() == 0)
		{			
			ActionErrors actionErrors = new ActionErrors();
			actionErrors.add("StudentNotEnroled", new ActionError("error.students.Mark.NotAvailable"));
			saveErrors(request, actionErrors);
			return chooseCurricularCourses(mapping, form,request,response);
		}

		InfoEnrolment oneInfoEnrollment= (InfoEnrolment) listEnrolmentEvaluation.get(0);
		request.setAttribute("oneInfoEnrollment", oneInfoEnrollment);
		
		return mapping.findForward("showMarksManagementMenu");
	}
	
	public static String getFromRequest(String parameter, HttpServletRequest request)
	{
		String parameterString = request.getParameter(parameter);
		if (parameterString == null)
		{
			parameterString = (String) request.getAttribute(parameter);
		}
		if(parameterString != null) {
			request.setAttribute(parameter, parameterString);
		}
		return parameterString;
	}

	
}