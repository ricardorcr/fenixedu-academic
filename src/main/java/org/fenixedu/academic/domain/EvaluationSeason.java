package org.fenixedu.academic.domain;

import java.util.stream.Stream;

import org.fenixedu.commons.i18n.LocalizedString;

public class EvaluationSeason extends EvaluationSeason_Base implements Comparable<EvaluationSeason> {

    public EvaluationSeason() {
        super();
        setEvaluationConfiguration(EvaluationConfiguration.getInstance());
    }

    public EvaluationSeason(LocalizedString acronym, LocalizedString name, boolean normal, boolean improvement,
            boolean specialAuthorization, boolean special) {
        this();
        setAcronym(acronym);
        setName(name);
        setNormal(normal);
        setImprovement(improvement);
        setSpecialAuthorization(specialAuthorization);
        setSpecial(special);
    }

    public boolean isNormal() {
        return getNormal();
    }

    public boolean isSpecial() {
        return getSpecial();
    }

    public boolean isImprovement() {
        return getImprovement();
    }

    public boolean isSpecialAuthorization() {
        return getSpecialAuthorization();
    }

    public static EvaluationSeason readDefaultSeasonForNewEnrolments() {
        return all().filter(EvaluationSeason::isNormal).findAny().orElse(null);
    }

    public static EvaluationSeason readNormalSeason() {
        return all().filter(EvaluationSeason::isNormal).findAny().orElse(null);
    }

    public static EvaluationSeason readSpecialSeason() {
        return all().filter(EvaluationSeason::isSpecial).findAny().orElse(null);
    }

    public static EvaluationSeason readImprovementSeason() {
        return all().filter(EvaluationSeason::isImprovement).findAny().orElse(null);
    }

    public static EvaluationSeason readSpecialAuthorization() {
        return all().filter(EvaluationSeason::isSpecialAuthorization).findAny().orElse(null);
    }

    public static Stream<EvaluationSeason> all() {
        return EvaluationConfiguration.getInstance().getEvaluationSeasonSet().stream();
    }

    public Stream<OccupationPeriod> getExamPeriods(ExecutionDegree executionDegree, ExecutionSemester semester) {
        return executionDegree
                .getPeriodReferences(OccupationPeriodType.EXAMS, semester == null ? null : semester.getSemester(), null)
                .filter(r -> r.getEvaluationSeasonSet().contains(this)).distinct()
                .map(OccupationPeriodReference::getOccupationPeriod);
    }

    public boolean isGradeSubmissionAvailable(CurricularCourse curricularCourse, ExecutionSemester semester) {
        final ExecutionDegree executionDegree = curricularCourse.getExecutionDegreeFor(semester.getExecutionYear());
        return executionDegree
                .getPeriodReferences(OccupationPeriodType.GRADE_SUBMISSION, semester == null ? null : semester.getSemester(),
                        null).filter(r -> r.getEvaluationSeasonSet().contains(this)).distinct()
                .map(OccupationPeriodReference::getOccupationPeriod).anyMatch(o -> o.getPeriodInterval().containsNow());
    }

    public Stream<OccupationPeriod> getGradeSubmissionPeriods(ExecutionDegree executionDegree, ExecutionSemester semester) {
        return executionDegree
                .getPeriodReferences(OccupationPeriodType.GRADE_SUBMISSION, semester == null ? null : semester.getSemester(),
                        null).filter(r -> r.getEvaluationSeasonSet().contains(this)).distinct()
                .map(OccupationPeriodReference::getOccupationPeriod);
    }

    @Override
    public int compareTo(EvaluationSeason o) {
        return getExternalId().compareTo(o.getExternalId());
    }
}
