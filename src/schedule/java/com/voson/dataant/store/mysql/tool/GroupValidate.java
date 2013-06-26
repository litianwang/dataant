package com.voson.dataant.store.mysql.tool;

import com.voson.dataant.model.GroupDescriptor;
import com.voson.dataant.util.ZeusException;

public class GroupValidate {
	public static boolean valide(GroupDescriptor group) throws ZeusException{
		if(group.getName()==null || group.getName().trim().equals("")){
			throw new ZeusException("name字段不能为空");
		}
		
		return true;
	}
}
