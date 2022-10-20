package com.defi.dextools.utils;

import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.EthCall;
import org.web3j.protocol.http.HttpService;

import java.util.ArrayList;
import java.util.List;

public class Web3jUtils {

    public static void main(String[] args) {
        String ethUrl = "https://eth-mainnet.g.alchemy.com/v2/Sy_rK-LrvNUNo9hXLf6GUKFp4Jk-iiZd";
        String polygonUrl = "https://polygon-mainnet.g.alchemy.com/v2/4ejnwaF6Ia19m9al8UC3W1rwhJShLMwV";

        Web3j web3jNode = Web3j.build(new HttpService(ethUrl));

//请求sender地址
        String fromAddress = "0x6fd7f278d2F1CcBe291fC6AA4e16278436Ab7e0C";
//USDT合约地址
        String contractAddress = "0xdac17f958d2ee523a2206206994597c13d831ec7";

        List<Type> input = new ArrayList<>();
//返回的是字符串，如果返回格式错误下面解析将会报错
        List<TypeReference<?>> output = new ArrayList<>();

        output.add(new TypeReference<Uint256>() {
        });

        Function function = new Function("totalSupply", input, output);
        String data = FunctionEncoder.encode(function);

        List<Type> res = null;
        try {
            // 组建请求的参数   调用者地址，合约地址、参数
            EthCall response = web3jNode.ethCall(
                    Transaction.createEthCallTransaction(fromAddress, contractAddress, data),
                    DefaultBlockParameterName.LATEST)
                    .send();
            res = FunctionReturnDecoder.decode(response.getValue(), function.getOutputParameters());
        } catch (Exception e) {
            System.out.println(e);
        }

        for (Type type : res) {
            System.out.println(type.getValue());
        }
    }
}
