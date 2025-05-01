package com.test.card_system.api.exception;

import com.test.card_system.core.exception.UserAlreadyExistsException;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationErrors(@NotNull MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String field = ((FieldError) error).getField();
            String message = error.getDefaultMessage();
            errors.put(field, message);
        });
        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<String> handleUserAlreadyExistsException(@NotNull Exception ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }






    /*} catch (SignatureException e) {
            // Неверная подпись
            logger.error("Invalid JWT signature: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            // Неправильный формат токена
            logger.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            // Токен просрочен
            logger.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            // Неподдерживаемый токен
            logger.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            // Пустой или null токен
            logger.error("JWT claims string is empty: {}", e.getMessage());
        }*/


    /*} catch (ExpiredJwtException | MalformedJwtException | SecurityException e) {
            log.warn("Invalid or expired token: {}", e.getMessage());
            throw new InvalidTokenException("Invalid or expired token");
        } catch (Exception e) {
            log.error("Failed to validate token: {}", e.getMessage(), e);
            throw new ServerException("Failed to validate token");
        }

        catch (ExpiredJwtException | MalformedJwtException | SecurityException e) {
            log.warn("Invalid or expired refresh token: {}", e.getMessage());
            throw new InvalidTokenException("Invalid or expired refresh token");
        } catch (Exception e) {
            log.error("Failed to refresh token: {}", e.getMessage(), e);
            throw new ServerException("Failed to refresh token");*/


//        Jws<Claims> parseClaimsJws(String var1) throws
//        ExpiredJwtException, UnsupportedJwtException, MalformedJwtException, SignatureException, IllegalArgumentException;

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleOtherExceptions(@NotNull Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal error: " + ex.getMessage());
    }
}
