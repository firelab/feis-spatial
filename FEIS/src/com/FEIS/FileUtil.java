package com.FEIS;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.logging.Logger;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;

/*
 * Read Static HTML file for output in Master Page
 */
@ManagedBean(name = "fileUtilBean")
@SessionScoped
public class FileUtil implements Serializable {
	private final Logger log = Logger.getLogger(this.getClass().getName());

	public String ReadFileAsString(String fileName) throws java.io.IOException {

		ServletContext ctx = (ServletContext) FacesContext.getCurrentInstance()
				.getExternalContext().getContext();
		String realPath = ctx.getRealPath("/");
		String srcDrive = "";
		if (realPath != null && !realPath.isEmpty())
			srcDrive = realPath.substring(0, 2);

		String fileFolder = ctx.getInitParameter("html-path");
		String fullPath = "";
		if (fileFolder != null)
			fullPath = srcDrive + fileFolder + fileName;
		else
			fullPath = srcDrive + fileName;
		System.out.println(fullPath);
		byte[] buffer = new byte[(int) new File(fullPath).length()];
		BufferedInputStream f = null;
		try {

			f = new BufferedInputStream(new FileInputStream(fullPath));
			f.read(buffer);
		} finally {
			if (f != null)

				try {
					f.close();
				} catch (IOException e) {
					log.info("Unable to find html files for Web Pages "
							+ e.getMessage());
					e.printStackTrace();
				}
		}

		return new String(buffer);

	}
}
