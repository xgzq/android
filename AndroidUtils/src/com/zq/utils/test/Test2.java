package com.zq.utils.test;

public class Test2 {

	private String name;
	private int age;
	private Test t;

	public Test2(String name, int age, Test t) {
		this.name = name;
		this.age = age;
		this.t = t;
	}

	@Override
	public String toString() {
		return "Test2 [name=" + name + ", age=" + age + ", t=" + t + "]";
	}

}
