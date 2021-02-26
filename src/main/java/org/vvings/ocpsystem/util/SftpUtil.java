package org.vvings.ocpsystem.util;

import net.schmizz.sshj.SSHClient;
import net.schmizz.sshj.sftp.SFTPClient;
import net.schmizz.sshj.transport.verification.PromiscuousVerifier;
import net.schmizz.sshj.xfer.FileSystemFile;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;

/**
 * @Author vvings
 * @Date 2020/12/22 23:50
 * @Version 1.0
 */
@Component
public class SftpUtil {

    private static String username;

    private static String password;

    private static String desc;

    private static String ip;
    @Value("${sftp.username}")
    public  void setUsername(String username) {
        SftpUtil.username = username;
    }
    @Value("${sftp.password}")
    public  void setPassword(String password) {
        SftpUtil.password = password;
    }
    @Value("${sftp.dest}")
    public  void setDesc(String desc) {
        SftpUtil.desc = desc;
    }
    @Value("${sftp.ip}")
    public  void setIp(String ip) {
        SftpUtil.ip = ip;
    }

    public static Boolean uploadFile(File file){
        final SSHClient ssh = new SSHClient();
        ssh.addHostKeyVerifier(new PromiscuousVerifier());
        try {
            ssh.connect(ip);
            ssh.authPassword(username,password);
            final SFTPClient sftp = ssh.newSFTPClient();
            sftp.put(new FileSystemFile(file),desc);
            ssh.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

    }
}
