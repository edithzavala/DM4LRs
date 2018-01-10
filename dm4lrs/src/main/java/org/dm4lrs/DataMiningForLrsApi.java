package org.dm4lrs;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/weka")
public class DataMiningForLrsApi {

  // http://localhost:8080/weka/jrip/csv?fileName=name&class=attribute
  @GetMapping(path = "/jrip/csv")
  public String extractRulesWithJrip(
      @RequestParam(value = "fileName", required = true) String fileName,
      @RequestParam(value = "class", required = true) String classAttribute) {
    return WekaApiExecutor.runDataMining("JRip", fileName, "CSV", classAttribute);
  }
}
