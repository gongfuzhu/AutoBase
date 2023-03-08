package com.gongfuzhu.autotools.core.tools;

import com.jcraft.jsch.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class SSHTool {
    private String user;
    private String password = "";
    private String host;
    private int port = 22;
    private String privateKeyPath;
    private String passphrase;
    private JSch jsch;

    public SSHTool(String user, String password, String host) {
        this.user = user;
        this.password = password;
        this.host = host;
        this.jsch = new JSch();
    }

    public SSHTool(String user, String password, String host, int port) {
        this.user = user;
        this.password = password;
        this.host = host;
        this.port = port;
        this.jsch = new JSch();
    }

    public SSHTool(String user, String host, String privateKeyPath, String passphrase) throws JSchException {
        this.user = user;
        this.privateKeyPath = privateKeyPath;
        this.passphrase = passphrase;
        this.host = host;
        this.jsch = new JSch();
        // 加载私钥
        jsch.addIdentity(privateKeyPath, passphrase);
    }
    public SSHTool(String user, String host,int port, String privateKeyPath, String passphrase) throws JSchException {
        this.user = user;
        this.privateKeyPath = privateKeyPath;
        this.passphrase = passphrase;
        this.host = host;
        this.port = port;
        this.jsch = new JSch();
        // 加载私钥
        jsch.addIdentity(privateKeyPath, passphrase);
    }

    public String executeCommand(String command) {
        if (command.startsWith("rm")) {
            return ShellType.Faile.remark;
        }
        StringBuilder output = new StringBuilder();
        try {
            // 建立SSH连接
            Session session = jsch.getSession(user, host, port);
            session.setPassword(password);
            session.setConfig("StrictHostKeyChecking", "no");
            session.connect();

            // 打开通道
            Channel channel = session.openChannel("exec");

            // 执行命令
            ((ChannelExec) channel).setCommand(command);
            channel.setInputStream(null);
            ((ChannelExec) channel).setErrStream(System.err);

            // 获取命令输出
            InputStream in = channel.getInputStream();
            channel.connect();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line);
                output.append("\n");
            }

            // 关闭通道和SSH连接
            channel.disconnect();
            session.disconnect();
        } catch (JSchException | IOException e) {
            e.printStackTrace();
        }
        return output.toString();
    }

    enum ShellType {

        Faile("禁止删除");

        String remark;

        ShellType(String remark) {
            this.remark = remark;
        }

    }
}

