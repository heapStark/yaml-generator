package service.test;


import org.springframework.web.bind.annotation.*;
import service.bean.ServiceTerm;
import service.bean.ServiceTermResult;
import service.result.OpenApiResult;

import java.util.Map;

/**
 * jcloud-renewal
 * Created by wangzhilei3 on 2018/1/9.
 */
@RestController
@RequestMapping(value = "/renewal3")
public class OpenApiServiceTermController5 {

    @RequestMapping(path = "/queryServiceTerm/{serviceCode}", method = RequestMethod.GET)
    public OpenApiResult<ServiceTermResult> query(@PathVariable("serviceCode") String serviceCode) {
        return null;
    }

    @RequestMapping(path = "/query/{serviceCode}:delete", method = RequestMethod.GET)
    public OpenApiResult<ServiceTermResult> query(@PathVariable(value = "serviceCode") String serviceCode,
                                                  @RequestParam(value = "filters", required = false) Map<String, String> filters,
                                                  @RequestParam(value = "pageNumber", required = false) Integer pageNumber,
                                                  @RequestParam(value = "pageSize") Integer pageSize,
                                                  @RequestHeader(value = "x-jcloud-pin") String pin,
                                                  @RequestHeader(value = "x-jcloud-erp", required = false) String erp
    ) {
        return null;
    }


    @RequestMapping(path = "/postQuery/{serviceCode}", method = RequestMethod.POST)
    public OpenApiResult<ServiceTermResult> postQuery(@PathVariable(value = "serviceCode") String serviceCode,
                                                      @RequestParam(value = "filters", required = false) Map<String, String> filters,
                                                      @RequestParam(value = "pageNumber", required = false) Integer pageNumber,
                                                      @RequestParam(value = "pageSize") Integer pageSize,
                                                      @RequestHeader(value = "x-jcloud-pin") String pin,
                                                      @RequestHeader(value = "x-jcloud-erp", required = false) String erp,
                                                      @RequestBody() ServiceTerm serviceTerm
    ) {
        return null;
    }

    @RequestMapping(path = "/queryServiceTerm/{serviceCode}", method = RequestMethod.DELETE)
    public OpenApiResult<ServiceTermResult> delete(@PathVariable("serviceCode") String serviceCode) {
        return null;
    }

    @RequestMapping(path = "/queryServiceTerm/{serviceCode}", method = RequestMethod.PUT)
    public OpenApiResult<ServiceTermResult> put(@PathVariable("serviceCode") String serviceCode) {
        return null;
    }

    @RequestMapping(path = "/queryServiceTerm/{serviceCode}", method = RequestMethod.PATCH)
    public OpenApiResult<ServiceTermResult> patch(@PathVariable("serviceCode") String serviceCode) {
        return null;
    }

    @RequestMapping(path = "/queryServiceTerm/{serviceCode}", method = RequestMethod.PATCH)
    public OpenApiResult<ServiceTerm> test(@PathVariable("serviceCode") String serviceCode) {
        return null;
    }



}