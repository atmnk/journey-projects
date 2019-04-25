package com.atmaram.jp.cli;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.io.IOException;
import java.io.PrintWriter;

public class LogHelper {
    public static void writelogs(JSONArray logObj, JSONObject jsonObject) throws IOException {
        JsonWriter writer=new JsonWriter();
        jsonObject.writeJSONString(writer);
        JsonWriter logWritter=new JsonWriter();
        logObj.writeJSONString(logWritter);
        PrintWriter out=new PrintWriter("log/log.json");
        PrintWriter html=new PrintWriter("log/index.html");
        html.print("<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">\n" +
                "    <style>\n" +
                "        ul, #myUL {\n" +
                "            list-style-type: none;\n" +
                "        }\n" +
                "\n" +
                "        #myUL {\n" +
                "            margin: 0;\n" +
                "            padding: 0;\n" +
                "        }\n" +
                "\n" +
                "        .caret {\n" +
                "            cursor: pointer;\n" +
                "            -webkit-user-select: none; /* Safari 3.1+ */\n" +
                "            -moz-user-select: none; /* Firefox 2+ */\n" +
                "            -ms-user-select: none; /* IE 10+ */\n" +
                "            user-select: none;\n" +
                "        }\n" +
                "\n" +
                "        .caret::before {\n" +
                "            content: \"\\25B6\";\n" +
                "            color: black;\n" +
                "            display: inline-block;\n" +
                "            margin-right: 6px;\n" +
                "        }\n" +
                "\n" +
                "        .caret-down::before {\n" +
                "            -ms-transform: rotate(90deg); /* IE 9 */\n" +
                "            -webkit-transform: rotate(90deg); /* Safari */'\n" +
                "        transform: rotate(90deg);\n" +
                "        }\n" +
                "\n" +
                "        .nested {\n" +
                "            display: none;\n" +
                "        }\n" +
                "\n" +
                "        .active {\n" +
                "            display: block;\n" +
                "        }\n" +
                "    </style>\n" +
                "</head>\n" +
                "<body>\n" +
                "<div id=\"view\">\n" +
                "\n" +
                "</div>\n" +
                "<script src=\"https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js\"></script>\n" +
                "<script>\n" +
                "var json="+logWritter.toString()+";"
                +
                "    function buildrestObject(object){\n" +
                "        var ul=$(\"<ul/>\");\n" +
                "        if(Array.isArray(object)){\n" +
                "            var i;\n" +
                "            for(i=0;i<object.length;i++){\n" +
                "                var item=$(\"<li/>\");\n" +
                "                ul.append(item)\n" +
                "                var sp=$(\"<span/>\")\n" +
                "                sp.addClass(\"caret\");\n" +
                "                sp.text(i);\n" +
                "                item.append(sp);\n" +
                "                item.append(buildrestObject(object[i]).addClass('nested'));\n" +
                "            }\n" +
                "        } else if(object instanceof Object) {\n" +
                "            for(var key in object){\n" +
                "                var li=$(\"<li/>\");\n" +
                "                var span=$(\"<span><b>\"+key+\"</b>:&nbsp</span>\");\n" +
                "                li.append(span);\n" +
                "                if(object[key] instanceof Object){\n" +
                "                    li.append(buildrestObject(object[key]));\n" +
                "                } else {\n" +
                "                    span.append(object[key]);\n" +
                "                }\n" +
                "                ul.append(li);\n" +
                "            }\n" +
                "        } else {\n" +
                "            return $(\"<span>\"+object+\"</span>\");\n" +
                "        }\n" +
                "        return ul;\n" +
                "    }\n" +
                "    function buildLI(object) {\n" +
                "        var item=$(\"<li/>\");\n" +
                "        if (!(object.type == 'get' || object.type == 'post' || object.type == 'patch' || object.type == 'put' || object.type == 'delete')){\n" +
                "            var sp=$(\"<span/>\")\n" +
                "            sp.addClass(\"caret\");\n" +
                "            sp.append(\"<b>\"+object.name + \"(\" + object.type + \")</b>\");\n" +
                "            item.append(sp);\n" +
                "            if(object.type=='command'){\n" +
                "                item.append(buildTree(object.steps).addClass('nested'));\n" +
                "            } else if(object.type=='block'){\n" +
                "                item.append(buildArray(object.iterations).addClass('nested'));\n" +
                "            } else if(object.type=='file'){\n" +
                "                item.append(buildFile(object.iterations).addClass('nested'));\n" +
                "            }else if(object.type=='loop'){\n" +
                "                item.append(buildArray(object.iterations).addClass('nested'));\n" +
                "            }else if(object.type=='poll'){\n" +
                "                item.append(buildTree(object.iterations).addClass('nested'));\n" +
                "            }\n" +
                "        }\n" +
                "        else {\n" +
                "            item.append(\"<span class='caret'><b>\"+object.name+\"(\"+object.type+\")\"+\"</b></span>\");\n" +
                "            item.append(buildrestObject(object).addClass('nested'));\n" +
                "        }\n" +
                "        return item;\n" +
                "    }\n" +
                "    function buildArray(json){\n" +
                "        var tree=$(\"<ul/>\");\n" +
                "        var i;\n" +
                "        for(i=0;i<json.length;i++){\n" +
                "            var item=$(\"<li/>\");\n" +
                "            tree.append(item)\n" +
                "            var sp=$(\"<span/>\")\n" +
                "            sp.addClass(\"caret\");\n" +
                "            sp.text(\"Iteration \"+i);\n" +
                "            item.append(sp);\n" +
                "            item.append(buildTree(json[i]).addClass('nested'));\n" +
                "        }\n" +
                "        return tree;\n" +
                "    }\n" +
                "    function buildFile(json){\n" +
                "        var tree=$(\"<ul/>\");\n" +
                "        var i;\n" +
                "        for(i=0;i<json.length;i++){\n" +
                "            var item=$(\"<li/>\");\n" +
                "            tree.append(item)\n" +
                "            var sp=$(\"<span/>\")\n" +
                "            sp.addClass(\"caret\");\n" +
                "            sp.text(\"Iteration \"+i);\n" +
                "            item.append(sp);\n" +
                "            item.append(json[i].value);\n" +
                "        }\n" +
                "        return tree;\n" +
                "    }\n" +
                "    function buildTree(json){\n" +
                "        var tree=$(\"<ul/>\");\n" +
                "        var i;\n" +
                "        for(i=0;i<json.length;i++){\n" +
                "            tree.append(buildLI(json[i]));\n" +
                "        }\n" +
                "        return tree;\n" +
                "    }\n" +
                "        $(\"#view\").append(buildTree(json))\n" +
                "        console.log(json); // this will show the info it in firebug console\n" +
                "        var toggler = document.getElementsByClassName(\"caret\");\n" +
                "        var i;\n" +
                "\n" +
                "        for (i = 0; i < toggler.length; i++) {\n" +
                "            toggler[i].addEventListener(\"click\", function() {\n" +
                "                this.parentElement.querySelector(\".nested\").classList.toggle(\"active\");\n" +
                "                this.classList.toggle(\"caret-down\");\n" +
                "            });\n" +
                "        }\n" +
                "\n" +
                "</script>\n" +
                "</body>\n" +
                "</html>\n");
        out.print(logWritter.toString());
        html.flush();
        out.flush();
        html.close();
        out.close();
        System.out.println(writer.toString());
//        writeToClipboard(writer.toString(),null);
    }
    public static void writeToClipboard(String s, ClipboardOwner owner) {
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        Transferable transferable = new StringSelection(s);
        clipboard.setContents(transferable, owner);
    }
}
