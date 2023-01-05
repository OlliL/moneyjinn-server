package org.laladev.moneyjinn.server.controller.impl;

import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/moneyflow/server/csrf/")
public class CsrfController {
  @RequestMapping("/csrf")
  public CsrfToken csrf(final CsrfToken token) {
    return token;
  }
}
