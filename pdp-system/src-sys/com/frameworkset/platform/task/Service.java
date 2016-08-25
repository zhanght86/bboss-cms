package com.frameworkset.platform.task;

import java.io.Serializable;

public interface Service extends Serializable {
	public void startService();
	public void restartService();

}
