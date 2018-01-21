package gtu.webservice.eg1;

import gtu.webservice.eg1.generate.CalculatorService;

public class CalculatorClient {
    public static void main(String[] args) {
        //先於cmd輸入 wsimport -keep http://localhost:8088/calcuator?wsdl -d c: -verbose'
        //檔案預設會放在~localhost目錄底下
        CalculatorService service = new CalculatorService();
        gtu.webservice.eg1.generate.Calculator calculatorProxy = service.getCalculatorPort();
        int result = calculatorProxy.add(10,20);
        System.out.println("Sum of 10+20 = " + result);
    }
}