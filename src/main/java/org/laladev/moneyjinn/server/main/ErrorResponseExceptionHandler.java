package org.laladev.moneyjinn.server.main;

import javax.servlet.http.HttpServletRequest;

import org.laladev.moneyjinn.businesslogic.model.exception.BusinessException;
import org.laladev.moneyjinn.core.rest.model.ErrorResponse;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ErrorResponseExceptionHandler extends ResponseEntityExceptionHandler implements BeanPostProcessor {
	@ExceptionHandler(BusinessException.class)
	@ResponseBody
	ResponseEntity<Object> handleControllerException(final HttpServletRequest req, final BusinessException ex) {
		final ErrorResponse errorResponse = new ErrorResponse();
		errorResponse.setCode(ex.getErrorCode().getErrorCode());
		errorResponse.setMessage(ex.getErrorMessage());

		return new ResponseEntity<Object>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@Override
	public Object postProcessBeforeInitialization(final Object bean, final String beanName) throws BeansException {
		if (bean instanceof DispatcherServlet) { // otherwise we get a 404 before our exception
													// handler kicks in
			((DispatcherServlet) bean).setThrowExceptionIfNoHandlerFound(true);
		}
		return bean;
	}

	@Override
	public Object postProcessAfterInitialization(final Object bean, final String beanName) throws BeansException {
		return bean;
	}

}
