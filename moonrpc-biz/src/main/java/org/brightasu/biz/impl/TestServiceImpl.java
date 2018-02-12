package org.brightasu.biz.impl;

import org.brightasu.biz.TestService;

/**
 * MoonRpc.
 *
 * @Desc .
 * @Author Swift.
 * @Date 2018/2/12.
 */
public class TestServiceImpl extends TestService {
    @Override
    public void testRpcInvoke(){
        System.out.println("hello moonrpc");
    }

}
