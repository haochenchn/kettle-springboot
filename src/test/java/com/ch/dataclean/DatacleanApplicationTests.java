package com.ch.dataclean;

import com.ch.dataclean.common.kettle.KettleManager;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DatacleanApplicationTests {
	@Autowired
	private KettleManager kettleManager;

	@Test
	public void test1() throws Exception{
		Map<String, String> mmcsMap = new HashMap<>();
		mmcsMap.put("param", "D:\\Test");
		//kettleManager.callTrans("/test","获取文件名列表", mmcsMap, null);
	}

}
