package com.voson.dataant.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.voson.dataant.model.processer.Processer;
public class JobDescriptor implements Serializable{

	private static final long serialVersionUID = 1L;
	private Map<String, String> properties=new HashMap<String, String>();
	private String cronExpression;
	private List<String> dependencies=new ArrayList<String>();
	private String id;
	private String name;
	private String desc;
	private String groupId;
	private String owner; 
	private Boolean auto=false;
	private List<Map<String, String>> resources=new ArrayList<Map<String,String>>();
	
	private JobRunType jobRunType;
	private JobScheduleType jobScheduleType;
	
	private String script;
	
	private List<Processer> preProcessers=new ArrayList<Processer>();
	private List<Processer> postProcessers=new ArrayList<Processer>();
	
	public List<Map<String, String>> getResources() {
		return resources;
	}

	public String getCronExpression() {
		return cronExpression;
	}

	public List<String> getDependencies() {
		return dependencies;
	}

	public String getDesc() {
		return desc;
	}

	public String getGroupId() {
		return groupId;
	}

	public String getId() {
		return id;
	}

	public JobRunType getJobType() {
		return jobRunType;
	}

	public String getName() {
		return name;
	}

	public String getOwner() {
		return owner;
	}


	public JobScheduleType getScheduleType() {
		return jobScheduleType;
	}

	public boolean hasDependencies() {
		return !dependencies.isEmpty();
	}

	public void setCronExpression(String cronExpression) {
		this.cronExpression=cronExpression;
	}

	public void setDependencies(List<String> depends) {
		this.dependencies=depends;
	}

	public void setDesc(String desc) {
		this.desc=desc;
	}

	public void setJobType(JobRunType type) {
		this.jobRunType=type;
	}

	public void setName(String name) {
		this.name=name;
	}

	public void setOwner(String owner) {
		this.owner=owner;
	}

	public void setScheduleType(JobScheduleType type) {
		this.jobScheduleType=type;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setResources(List<Map<String, String>> resources) {
		this.resources = resources;
	}

	public Map<String, String> getProperties() {
		return properties;
	}

	public void setProperties(Map<String, String> properties) {
		this.properties = properties;
	}

	public Boolean getAuto() {
		return auto;
	}

	public void setAuto(Boolean auto) {
		this.auto = auto;
	}

	public String getScript() {
		return script;
	}

	public void setScript(String script) {
		this.script = script;
	}
	public enum JobRunType{
		MapReduce("main"),Shell("shell"),Hive("hive");
		private final String id;
		JobRunType(String s){
			this.id=s;
		}
		@Override
		public String toString() {
			return id;
		}
		public static JobRunType parser(String v){
			for(JobRunType type:JobRunType.values()){
				if(type.toString().equals(v)){
					return type;
				}
			}
			return null;
		}
	}
	public enum JobScheduleType{
		 Independent(0),Dependent(1);
		 private Integer type;
		 private JobScheduleType(Integer type){
			 this.type=type;
		 }
		 @Override
		public String toString() {
			return type.toString();
		}
		 public static JobScheduleType parser(String value){
			 if("0".equals(value)){
				 return Independent;
			 }else if("1".equals(value)){
				 return Dependent;
			 }
			 return null;
		 }
		 public static JobScheduleType parser(Integer v){
			 for(JobScheduleType t:JobScheduleType.values()){
				 if(t.getType().equals(v)){
					 return t;
				 }
			 }
			 return null;
		 }
		public Integer getType() {
			return type;
		}
	}
	public List<Processer> getPreProcessers() {
		return preProcessers;
	}

	public void setPreProcessers(List<Processer> preProcessers) {
		this.preProcessers = preProcessers;
	}

	public List<Processer> getPostProcessers() {
		return postProcessers;
	}

	public void setPostProcessers(List<Processer> postProcessers) {
		this.postProcessers = postProcessers;
	}
}


	
	


