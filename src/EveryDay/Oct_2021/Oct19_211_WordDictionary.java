package EveryDay.Oct_2021;

/*
211. 添加与搜索单词 - 数据结构设计
请你设计一个数据结构，支持 添加新单词 和 查找字符串是否与任何先前添加的字符串匹配 。

实现词典类 WordDictionary ：

WordDictionary() 初始化词典对象
void addWord(word) 将 word 添加到数据结构中，之后可以对它进行匹配
bool search(word) 如果数据结构中存在字符串与 word 匹配，则返回 true ；否则，返回  false 。word 中可能包含一些 '.' ，每个 . 都可以表示任何一个字母。
 */

public class Oct19_211_WordDictionary {
    private Trie root;

    public Oct19_211_WordDictionary() {
        root = new Trie();
    }

    public void addWord(String word) {
        root.insert(word);
    }

    public boolean search(String word) {
        return dfs(word, 0, root);
    }

    public boolean dfs(String word, int index, Trie node)
    {
        if (index == word.length()) return node.getIsEnd();
        char ch = word.charAt(index);
        if (ch != '.')
        {
            int childrenIndex = ch - 'a';
            Trie child = node.getChildren()[childrenIndex];
            if (child != null && dfs(word, index+1, child)) return true;
        }
        else {
            for (int i = 0; i < 26; i++)
            {
                Trie child = node.getChildren()[i];
                if (child != null && dfs(word, index+1, child)) return true;
            }
        }

        return false;
    }
}

class Trie {
    private Trie[] children;
    private boolean isEnd;

    public Trie() {
        children = new Trie[26];
        isEnd = false;
    }

    public void insert(String word) {
        Trie node = this;
        for (int i = 0; i < word.length(); i++)
        {
            char ch = word.charAt(i);
            int index = ch - 'a';
            if (node.children[index] == null) node.children[index] = new Trie();
            node = node.children[index];
        }
        node.isEnd = true;
    }

    public boolean search(String word) {
        Trie node = this;
        for (int i = 0; i < word.length(); i++)
        {
            node = node.children[word.charAt(i) - 'a'];
            if (node == null) return false;
        }
        return node.isEnd;
    }

    public boolean startsWith(String prefix) {
        Trie node = this;
        for (int i = 0; i < prefix.length(); i++)
        {
            node = node.children[prefix.charAt(i) - 'a'];
            if (node == null) return false;
        }
        return true;
    }

    public Trie[] getChildren(){
        return children;
    }

    public boolean getIsEnd(){
        return isEnd;
    }
}
