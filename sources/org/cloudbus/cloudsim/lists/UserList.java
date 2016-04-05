package org.cloudbus.cloudsim.lists;

import java.util.List;

import org.cloudbus.cloudsim.User;

public class UserList {
	public static <T extends User> T getById(List<T> userList, int id) {
		for (T user : userList) {
			if (user.getuserId() == id) {
				return user;
			}
		}
		return null;
	}
	public static <T extends User> int getPositionById(List<T> userList, int id) {
		int i = 0 ;
	        for (T user : userList) {
			if (user.getuserId() == id) {
				return i;
			}
			i++;
		}
		return -1;
	}
}
