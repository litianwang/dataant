package com.voson.dataant.util;
/**
 * Job运行所需要的配置信息
 * @author litianwang
 *
 */
public class RunningJobKeys {
	/**
	 * 执行JAVA的Main类路径
	 */
	public static final String RUN_JAVA_MAIN_CLASS = "java.main.class";
	/**
	 * JVM配置参数
	 */
	public static final String RUN_INITIAL_MEMORY_SIZE = "java.Xms";
	/**
	 * JVM配置参数
	 */
	public static final String RUN_MAX_MEMORY_SIZE = "java.Xmx";
	/**
	 * Main方法传入的参数
	 */
	public static final String RUN_JAVA_MAIN_ARGS = "java.main.args";
	/**
	 * JVM启动参数
	 */
	public static final String RUN_JVM_PARAMS = "java.jvm.args";
	/**
	 * Classpath路径
	 */
	public static final String RUN_CLASSPATH="java.classpath";
	/**
	 * 需要执行的shell文件路径
	 */
	public static final String RUN_SHELLPATH="shell.localfile";
	/**
	 * 需要执行的hive文件路径
	 */
	public static final String RUN_HIVE_PATH="hive.localfile";
	/**
	 * 需要执行的odps文件路径
	 */
	public static final String RUN_ODPS_PATH="odps.localfile";
}
