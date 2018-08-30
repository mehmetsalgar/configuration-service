package org.salgar.configuration.driver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DriverRestController {
    @Autowired
    private DriverFacade driverFacade;

    @RequestMapping("/driver")
    public String driverOperation() {
        return driverFacade.driver();
    }

    @RequestMapping("/driver1")
    public String driverOperation1() {
        return driverFacade.driver1();
    }
}
