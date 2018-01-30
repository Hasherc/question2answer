package com.qming.question2answer.service;

import org.apache.commons.lang3.CharUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: qming_c
 * Date: 2018-01-25
 * Time: 19:49
 */
@Service
public class SensitiveService implements InitializingBean {

    private static final Logger logger = LoggerFactory.getLogger(InitializingBean.class);

    private static final String DEFAULT_REPLACEMENT = "***";
    private TreeNode root;

    public static void main(String[] args) throws Exception {
        SensitiveService sensitiveService = new SensitiveService();
        sensitiveService.afterPropertiesSet();
        System.out.println(sensitiveService.isSensitive("好&色#之#赌博"));
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        root = new TreeNode();

        try {
            InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("sensitive.txt");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String word;
            while ((word = bufferedReader.readLine()) != null) {
                word = word.trim();
                addWord(word);
            }
            bufferedReader.close();
            inputStream.close();
            inputStream.close();
        } catch (Exception e) {
            logger.error("读取敏感词文件错误" + e.getMessage());
        }


    }

    private void addWord(String word) {
        TreeNode concurrentNode = root;
        Character character;
        for (int i = 0; i < word.length(); i++) {
            character = word.charAt(i);
            TreeNode node = concurrentNode.getSubNode(character);
            if (node == null) {
                node = new TreeNode();
            }
            concurrentNode.addSubNode(character, node);
            concurrentNode = node;
            if (i == (word.length() - 1)) {
                concurrentNode.setKeyWordEnd();
            }
        }
    }

    public String sensitiveFilter(String text) {
        if (StringUtils.isBlank(text)) {
            return text;
        }
        StringBuilder stringBuilder = new StringBuilder();
        TreeNode tempNode = root;

        /**
         * index为假定敏感词开头
         */
        int index = 0;

        /**
         * position为当前搜索到假定敏感词的位置
         */
        int position = 0;
        Character c;
        while (index < text.length()) {

            c = text.charAt(position);

            if (isSymbol(c)) {

                if (tempNode == root) {
                    stringBuilder.append(c);
                    index++;
                }
                position++;
                continue;
            }
            tempNode = tempNode.getSubNode(c);
            //节点空是，说明无敏感词，回滚
            if (tempNode == null) {
                stringBuilder.append(text.charAt(index));
                index++;
                position = index;
                tempNode = root;
            } else if (tempNode.isEnd()) {//发现敏感词，回滚
                stringBuilder.append(DEFAULT_REPLACEMENT);
                index = position + 1;
                position = index;
                tempNode = root;

            } else {//未发现敏感词结尾，继续
                position++;
            }
            //position越界手动复原，index进入下一个字符
            if (position == text.length() && index < text.length()) {
                tempNode = root;
                stringBuilder.append(text.charAt(index));
                index++;
                position = index;
            }
        }
        return stringBuilder.toString();
    }

    private boolean isSymbol(char c) {
        int ic = (int) c;
        return !CharUtils.isAsciiAlphanumeric(c) && (ic < 0x2E80 || ic > 0x9FFF);
    }

    public boolean isSensitive(String text) {
        if (StringUtils.isBlank(text)) {
            return false;
        }
        int index = 0;
        int position = 0;
        TreeNode tempNode = root;
        Character character;
        while (index < text.length()) {
            character = text.charAt(position);
            if (isSymbol(character)) {
                if (tempNode == root) {
                    index++;
                }
                position++;
                continue;
            }

            tempNode = tempNode.getSubNode(character);

            if (tempNode == null) {
                tempNode = root;
                index++;
                position = index;
            } else if (tempNode.isEnd()) {
                return true;
            } else {
                position++;
            }
            if (position == text.length() && index < text.length()) {
                tempNode = root;
                index++;
                position = index;
            }
        }
        return false;
    }

    private class TreeNode {
        /**
         * key 是下一个字符， value 是其子节点
         */
        private Map<Character, TreeNode> subNodes = new HashMap<>();
        /**
         * 是否位敏感词结尾
         */
        private boolean end = false;


        public boolean isEnd() {
            return end;
        }

        public void setKeyWordEnd() {
            this.end = true;
        }

        public void addSubNode(Character character, TreeNode subNode) {
            subNodes.put(character, subNode);
        }

        public TreeNode getSubNode(Character character) {
            return subNodes.get(character);
        }
    }
}
