package com.dev.tuzhiqiang.utils

@Suppress("unused")
class RulesParser {

    @Suppress("PrivatePropertyName")
    private val FINISH_STATE = Pair<String?,Int>(null,-1)

    lateinit var rule: String
        private set
        get
    private var len:Int = 0
    private var index = 0

    private val spacing = arrayOf(' ','#',',')



    // 初始化Parser
    fun init(rule: String) {
        this.rule = rule
        this.len = rule.length
    }

    // 读取token并消费
    fun consume(): String? {
        return peekInternal().let {
            index = it.second
            it.first
        }
    }

    // 读取token不消费
    fun peek():String? {
        return peekInternal().first
    }

    private fun peekInternal(): Pair<String?,Int> {
        // 合理性判断
        while (index >= len) {
            return FINISH_STATE
        }
        // 以空格为基础记录
        val begin = index
        var end = begin
        while (
            end < len &&
            rule[end] != ' ' &&
            rule[end] != '(' &&
            rule[end] != '#' &&
            rule[end] != ')'
        ) {
            end++
        }
        val str = rule.substring(begin,end)
        when(rule[end]) {
            ' ' -> {
                while (end < len && rule[end] in spacing) {
                    end++
                }
            }
            '#' -> {
                end++
            }
            '(' -> {

            }
            ')' -> {
                end++
            }
        }

        return Pair(str,end)
    }

}