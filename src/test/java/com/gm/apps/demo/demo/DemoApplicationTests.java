package com.gm.apps.demo.demo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DemoApplicationTests {

	@Test
	public void contextLoads() {
	}

}


class A {
  void method() throws Exception {}

  void method1() throws RuntimeException {}
}


class B extends A {
  @Override
  void method() throws RuntimeException {}

//  @Override
//  void method1() throws Exception {}
}
