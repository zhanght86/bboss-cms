package com.frameworkset.platform.security;

import java.util.ArrayList;
import java.util.List;

public class AuthorResource {
	protected List<String> authorResources;
	public void addAuthorResource(String authorResource)
	{
		if(authorResources == null)
			authorResources = new ArrayList<String>();
		this.authorResources.add(authorResource);
	}
	public List<String> getAuthorResources() {
		return authorResources;
	}
}
