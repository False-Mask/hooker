package com.dev.tuzhiqiang.utils

@Suppress("unused")
class RulesParser {

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
        peekInternal()
        return consumeToken()
    }

    // 读取token不消费
    fun peek():String? {
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
                var end = index
                while (end < len && rule[end] != ')') {
                    end++
                }
                curToken = rule.substring(index, end)
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