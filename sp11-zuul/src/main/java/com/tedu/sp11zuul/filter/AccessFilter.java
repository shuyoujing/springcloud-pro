package com.tedu.sp11zuul.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.apache.commons.lang.StringUtils;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import cn.tedu.web.util.JsonResult;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
@Component
public class AccessFilter extends ZuulFilter {
    @Override
    public String filterType() {
        return FilterConstants.PRE_TYPE;
    }

    /**
     * 过滤器添加的顺序号
     *
     * @return 表示加到第6个位置
     */
    @Override
    public int filterOrder() {
        return 6;
    }

    /**
     * 针对当前请求判断， 判断当前请求是否要执行过滤代码
     *
     * @return
     */
    @Override
    public boolean shouldFilter() {
        RequestContext ctx = RequestContext.getCurrentContext();
        String serviceId = (String) ctx.get(FilterConstants.SERVICE_ID_KEY);

        return serviceId.equals("item-service") ? true : false;

    }

    @Override
    public Object run() throws ZuulException {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
        String token = request.getParameter("token");
            if (StringUtils.isEmpty(token)){
                //没有Token组织继续调用
                ctx.setSendZuulResponse(false);
                //发送提示，提示用户没有登录
                ctx.setResponseStatusCode(JsonResult.NOT_LOGIN);
                ctx.setResponseBody(JsonResult.err().code(JsonResult.NOT_LOGIN).msg("NOT LOGIN IN! ").toString());
            }
        return null;
    }
}
