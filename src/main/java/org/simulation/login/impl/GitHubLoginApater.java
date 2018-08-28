/**
 * Copyright 2017 [https://github.com/bluetata] All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the 'License'); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an 'AS IS' BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.simulation.login.impl;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.simulation.login.AbstractLogin;

import java.util.List;

/**
 * @date     08/24/18 17:22
 * @version  simulation-login version(1.0)</br>
 * @author   bluetata / Sekito.Lv@gmail.com</br>
 * @since    JDK 1.8</br>
 */
public class GitHubLoginApater extends AbstractLogin {

    // define default constructor
    public GitHubLoginApater(String userName, String password) {
        super(userName, password);
    }

    /**
     * 准备登录
     *
     * @throws Exception
     */
    @Override
    protected void readyLogin() throws Exception{
        HttpGet readyGet = null;
        try {
            String readyUrl = getReadyLoginUrl();
            readyGet = new HttpGet(readyUrl);
            HttpResponse response = getUserClient().execute(readyGet);
            HttpEntity entity = response.getEntity();
            String info = EntityUtils.toString(entity);
            Document doc = Jsoup.parseBodyFragment(info);
            List<Element> elesList = doc.select("form");  // 获取提交form表单，可以通过查看页面源码代码得知
            // 获取表单信息
            // lets make data map containing all the parameters and its values found in the form

            if (elesList != null && elesList.size() > 0) {

                for (Element e : elesList.get(0).getAllElements()) {
                    // 设置用户名
                    if (e.attr("name").equals("login")) {
                        e.attr("value", getUserName());
                    }
                    // 设置用户密码
                    if (e.attr("name").equals("password")) {
                        e.attr("value", getPassword());
                    }
                    // 排除空值表单属性
                    if (e.attr("name").length() > 0) {
                        readyParams.put(e.attr("name"), e.attr("value"));
                    }
                }
            }

        } catch (Exception e) {
            throw e;
        } finally {
            if (readyGet != null) {
                readyGet.releaseConnection();
            }
        }
    }

    /**
     * 执行登陆
     *
     * @return 登陆的结果
     */
    @Override
    protected int executeLogin() throws Exception{
        return 0;
    }

    /**
     * 登陆成功，进行测试
     *
     * @throws Exception
     */
    @Override
    protected void testLogin() throws Exception{

    }

    @Override
    protected String getLoginUrl() {
        // XXX: it's better to dynamically get the url. 08/28/2018 17:50 bluetata
        return "https://github.com/login";
    }

    @Override
    protected String getAuthCodeImageUrl() {
        return null;
    }

    @Override
    protected String getReadyLoginUrl() {
        return null;
    }
}
