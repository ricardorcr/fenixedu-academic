/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Academic.
 *
 * FenixEdu Academic is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Academic is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * Created on 9/Jul/2004
 *
 */
package org.fenixedu.academic.dto;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.fenixedu.academic.domain.Exam;
import org.fenixedu.academic.domain.space.WrittenEvaluationSpaceOccupation;

/**
 * @author Tânia Pousão
 * 
 */
public class InfoExamWithRoomOccupations extends InfoExam {

    @Override
    public void copyFromDomain(Exam exam) {
        super.copyFromDomain(exam);
        if (exam != null) {
            setWrittenEvaluationSpaceOccupations(copyIRoomOccupation2InfoRoomOccupation(exam
                    .getWrittenEvaluationSpaceOccupationsSet()));
        }
    }

    private List<InfoRoomOccupation> copyIRoomOccupation2InfoRoomOccupation(
            Collection<WrittenEvaluationSpaceOccupation> associatedRoomOccupation) {
        final List<InfoRoomOccupation> infoRoomOccupations = new ArrayList<InfoRoomOccupation>(associatedRoomOccupation.size());
        for (WrittenEvaluationSpaceOccupation roomOccupation : associatedRoomOccupation) {
            final InfoRoomOccupation infoRoomOccupation = InfoRoomOccupation.newInfoFromDomain(roomOccupation);
            if (infoRoomOccupation != null) {
                infoRoomOccupations.add(infoRoomOccupation);
            }
        }
        return infoRoomOccupations;
    }

    public static InfoExam newInfoFromDomain(Exam exam) {
        InfoExamWithRoomOccupations infoExam = null;
        if (exam != null) {
            infoExam = new InfoExamWithRoomOccupations();
            infoExam.copyFromDomain(exam);
        }
        return infoExam;
    }
}