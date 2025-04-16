import { HttpHeaders, HttpInterceptorFn } from "@angular/common/http";
import { Inject } from "@angular/core";
import { KeycloakService } from "../services/keycloak.service";

export const keycloakHttpInterceptor:HttpInterceptorFn = (req,next)=> {
    const keycloakService = Inject(KeycloakService);
    const token = keycloakService?.keycloak?.token;
    if(token){
        const authReq = req.clone({
            headers: new HttpHeaders({
              Authorization: `Bearer ${token}`
            })
          });
          return next(authReq);
    }
    return next(req);
}