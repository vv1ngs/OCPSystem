package org.vvings.ocpsystem;

import net.schmizz.sshj.SSHClient;
import net.schmizz.sshj.sftp.SFTPClient;
import net.schmizz.sshj.transport.verification.PromiscuousVerifier;
import net.schmizz.sshj.xfer.FileSystemFile;
import net.schmizz.sshj.xfer.LocalSourceFile;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.vvings.ocpsystem.dao.InstitutionMapper;
import org.vvings.ocpsystem.pojo.Category;


import java.io.File;
import java.io.IOException;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

//@SpringBootTest
class ocpSystemApplicationTests {
	@Autowired
	private InstitutionMapper institutionMapper;
	@Test

	void contextLoads() {
		//Institution institution=new Institution(1,1,"123",true,1);
		//institutionMapper.insert(institution);
	}
	@Test
	void sftpTest() throws IOException {
		final SSHClient ssh = new SSHClient();
		ssh.addHostKeyVerifier(new PromiscuousVerifier());
		ssh.connect("139.9.116.255",22);
		ssh.authPassword("vvings","myloverismmyu");
		try {
			final SFTPClient sftp = ssh.newSFTPClient();
			try {
				sftp.put(new FileSystemFile("C:\\Users\\22080\\Desktop\\power\\pyexe.txt"), "/ocp_file/");
			} finally {
				sftp.close();
			}
		} finally {
			ssh.disconnect();
		}

	}
	@Test
	void makeString(){
		StringBuilder stringBuilder=new StringBuilder();
		for (int i = 0; i < 44; i++) {

			stringBuilder.append("aid[]=").append(i).append("&");
		}
		System.out.println(stringBuilder.toString());
	}

}
