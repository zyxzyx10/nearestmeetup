package com.paul.zhang;

import static spark.Spark.get;

public class DockerDemo {
	public static void main(String[] args) {
		System.out.println("hello world!");
		get("/", (req,resp) -> {
			return "hello from DockerDemo";
		});
	}
}
