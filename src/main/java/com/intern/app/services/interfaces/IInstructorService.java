package com.intern.app.services.interfaces;

import com.github.javafaker.Bool;
import com.intern.app.models.dto.request.InstructorCreationRequest;
import com.intern.app.models.dto.response.ReturnResult;
import com.intern.app.models.entity.Instructor;

public interface IInstructorService {
    ReturnResult<Boolean> CreateInstructor(InstructorCreationRequest instructorCreationRequest);
}
