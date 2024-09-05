package com.cripto.project.presentation.config.security.aspects.qualifications;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.cripto.project.domain.dtos.consumes.QualificationDtoRequest;
import com.cripto.project.domain.services.IQualificattionService;

@Aspect
@Component
public class QualificationSecurityAspect {

    private final IQualificattionService qualificationService;
    private static final String UNAUTHORIZED = "Access unauthorized";

    public QualificationSecurityAspect(IQualificattionService qualificationService) {
        this.qualificationService = qualificationService;
    }

    @Before("@annotation(RequireQualificationOwnership) && args(qualificationId,..)")
    public void checkQualificationOwnership(Long qualificationId) throws AccessDeniedException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getPrincipal().toString();

        if (!qualificationService.isOwnerOfQualification(qualificationId, username)) {
            throw new AccessDeniedException(UNAUTHORIZED);
        }
    }

    @Before("@annotation(RequireCourseOwnership) && args(dto,..)")
    public void checkCourseOwnership(QualificationDtoRequest dto) throws AccessDeniedException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getPrincipal().toString();

        Long courseId = dto.getCourseId();  

        if (!qualificationService.isOwnerOfCourse(courseId, username)) {
            throw new AccessDeniedException(UNAUTHORIZED);
        }
    }

    @Before("@annotation(RequireCourseOwnership) && args(courseId,..)")
    public void checkCourseOwnership(Long courseId) throws AccessDeniedException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getPrincipal().toString();

        if (!qualificationService.isOwnerOfCourse(courseId, username)) {
            throw new AccessDeniedException(UNAUTHORIZED);
        }
    }
}
