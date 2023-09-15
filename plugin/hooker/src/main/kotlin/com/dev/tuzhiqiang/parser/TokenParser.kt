package com.dev.tuzhiqiang.parser

@Suppress("unused")
class TokenParser {

    lateinit var rule: String
        private set
    private var len:Int = 0
    private var index = 0

    private var curToken:String? = null

    private val spacing = arrayOf(' ','#',',')

    // 初始化Parser
    fun init(rule: String) {
        this.rule = rule
        this.len = rule.length
    }

    // 读取token并消费
    fun consume(): String? {
        if(curToken == null) {
            peekInternal()
        }
        return consumeToken()
    }

    // 读取token不消费
    fun peek():String? {
        if(curToken != null) {
            return curToken
        }
        peekInternal()
        return curToken
    }

    private fun consumeToken(): String? {
        val res = curToken
        index += curToken?.length ?: 0
        curToken = null
        return res
    }

    private fun peekInternal() {
        // 合理性判断
        while (index >= len) {
            return
        }
        // 以空格为基础记录
        // skip
        when(rule[index]) {
            ' ' -> {
                while (index < len && rule[index] == ' ') {
                    index++
                }
            }
            '#' -> {
                index++
            }
            '(' -> {
                val begin = index + 1
                var end = index
                while (end < len && rule[end] != ')') {
                    end++
                }
                curToken = rule.substring(begin, end)
                index = end + 1
                return
            }
            ')' -> {
                index++
            }
        }
        val begin = index
        var end = begin
        // read token
        while (
            end < len &&
            rule[end] != ' ' &&
            rule[end] != '(' &&
            rule[end] != '#' &&
            rule[end] != ')'
        ) {
            end++
        }
        curToken = rule.substring(begin,end)
    }

}