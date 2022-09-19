package com.defi.dextools.controller;

import com.defi.dextools.utils.HttpUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/get_blockhash")
    public String getLastBlockhash() {
        String blockhash = HttpUtils.getHttp("http://124.220.51.86/api/get_blockhash");
        System.out.println(blockhash);
        return blockhash;
    }

    @GetMapping("/send_tx")
    public String sendTx(@RequestParam(name = "tx") String tx) {
        String result = HttpUtils.getHttp("http://124.220.51.86/api/send_tx?tx=" + tx);
        System.out.println(result);
        return result;
    }
}
