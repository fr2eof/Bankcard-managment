package com.pet.card_system.api.exception;

import com.pet.card_system.core.dto.ErrorResponse;
import com.pet.card_system.core.exception.CardNotFoundException;
import com.pet.card_system.core.exception.RequestNotFoundException;
import com.pet.card_system.core.exception.UserAlreadyExistsException;
import com.pet.card_system.core.exception.UserNotFoundException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.validation.constraints.NotNull;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.Operation;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @Operation(summary = "Handle validation errors")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "Validation errors occurred")
    })
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationErrors(MethodArgumentNotValidException ex) {
        StringBuilder errorMessage = new StringBuilder("Validation failed: ");
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errorMessage.append(error.getField()).append(" ").append(error.getDefaultMessage()).append("; ")
        );
        return ResponseEntity.badRequest()
                .body(new ErrorResponse("VALIDATION_ERROR", errorMessage.toString()));
    }

    @Operation(summary = "Handle user already exists exception")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "409", description = "User already exists")
    })
    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleUserAlreadyExistsException(UserAlreadyExistsException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ErrorResponse("USER_ALREADY_EXISTS", ex.getMessage()));
    }

    @Operation(summary = "Handle user not found exception")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFound(UserNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse("USER_NOT_FOUND", ex.getMessage()));
    }

    @Operation(summary = "Handle request not found exception")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "404", description = "Request not found")
    })
    @ExceptionHandler(RequestNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleRequestNotFound(RequestNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse("REQUEST_NOT_FOUND", ex.getMessage()));
    }

    @Operation(summary = "Handle card not found exception")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "404", description = "Card not found")
    })
    @ExceptionHandler(CardNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleCardNotFound(CardNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse("CARD_NOT_FOUND", ex.getMessage()));
    }

    @Operation(summary = "Handle entity not found exception")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "404", description = "Entity not found")
    })
    @ExceptionHandler(EmptyResultDataAccessException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(EmptyResultDataAccessException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse("ENTITY_NOT_FOUND", "Resource was not found"));
    }

    @Operation(summary = "Handle invalid JWT signature exception")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "401", description = "Invalid JWT signature")
    })
    @ExceptionHandler({SignatureException.class})
    public ResponseEntity<ErrorResponse> handleInvalidSignature(SignatureException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ErrorResponse("INVALID_SIGNATURE", "Invalid JWT signature"));
    }

    @Operation(summary = "Handle malformed JWT token exception")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "Malformed JWT token")
    })
    @ExceptionHandler({MalformedJwtException.class})
    public ResponseEntity<ErrorResponse> handleMalformedToken(MalformedJwtException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse("MALFORMED_TOKEN", "JWT token is malformed"));
    }

    @Operation(summary = "Handle expired JWT token exception")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "401", description = "JWT token expired")
    })
    @ExceptionHandler({ExpiredJwtException.class})
    public ResponseEntity<ErrorResponse> handleExpiredToken(ExpiredJwtException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ErrorResponse("TOKEN_EXPIRED", "JWT token has expired"));
    }

    @Operation(summary = "Handle unsupported JWT token exception")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "Unsupported JWT token")
    })
    @ExceptionHandler({UnsupportedJwtException.class})
    public ResponseEntity<ErrorResponse> handleUnsupportedToken(UnsupportedJwtException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse("UNSUPPORTED_TOKEN", "JWT token is unsupported"));
    }

    @Operation(summary = "Handle empty JWT token exception")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "Empty JWT token")
    })
    @ExceptionHandler({IllegalArgumentException.class})
    public ResponseEntity<ErrorResponse> handleEmptyToken(IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse("EMPTY_TOKEN", "JWT claims string is empty or null"));
    }

    @Operation(summary = "Handle security exception")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "401", description = "Security exception")
    })
    @ExceptionHandler(SecurityException.class)
    public ResponseEntity<ErrorResponse> handleSecurityException(SecurityException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ErrorResponse("UNAUTHORIZED", "Security exception"));
    }

    @Operation(summary = "Handle general exceptions")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleOtherExceptions(@NotNull Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse("INTERNAL_ERROR", "Internal error: " + ex.getMessage()));
    }
}
