package com.voson.dataant.store.mysql.tool;

import com.voson.dataant.model.GroupDescriptor;
import com.voson.dataant.util.DataantException;

public class GroupValidate {
	public static boolean valide(GroupDescriptor group) throws DataantException{
		if(group.getName()==null || group.getName().trim().equals("")){
			throw new DataantException("name字段不能为空");
		}
		
		return true;
	}
}
