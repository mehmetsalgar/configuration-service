package org.salgar.configuration.scope;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.Scope;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.ParseException;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class RefreshScope implements Scope, BeanFactoryPostProcessor {
    private static final Log logger = LogFactory.getLog(RefreshScope.class);

    private ConcurrentMap<String, BeanWrapper> cache = new ConcurrentHashMap<>();
    private String name = "refresh";
    private StandardEvaluationContext evaluationContext;
    private ConfigurableListableBeanFactory beanFactory;
    private String id;

    @Override
    public Object get(String name, ObjectFactory<?> objectFactory) {
        BeanWrapper value =  this.cache.get(name);
        if(value == null) {
            value = new BeanWrapper(name, objectFactory);
            this.cache.put(name, value);
        }

        return value.getBean();
    }

    @Override
    public Object remove(String name) {
        BeanWrapper value = this.cache.remove(name);
        if(value == null) {
            return null;
        }
        return value.getBean();
    }

    @Override
    public void registerDestructionCallback(String name, final Runnable callback) {

    }

    @Override
    public Object resolveContextualObject(String key) {
        Expression expression = parseExpression(key);
        return expression.getValue(this.evaluationContext, this.beanFactory);

    }

    @Override
    public String getConversationId() {
        return this.name;
    }

    private Expression parseExpression(String input) {
        if(StringUtils.hasText(input)) {
            ExpressionParser parser = new SpelExpressionParser();
            try {
                return parser.parseExpression(input);
            } catch (ParseException e) {
                throw new IllegalArgumentException("Cannot parse expression: " + input,
                        e);
            }
        } else {
            return null;
        }
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        beanFactory.registerScope(this.name, this);
        setSerializationId(beanFactory);
    }

    private void setSerializationId(ConfigurableListableBeanFactory beanFactory) {
        if (beanFactory instanceof DefaultListableBeanFactory) {

            String id = this.id;
            if (id == null) {
                List<String> list = new ArrayList<>(
                        Arrays.asList(beanFactory.getBeanDefinitionNames()));
                Collections.sort(list);
                String names = list.toString();
                if(logger.isDebugEnabled()) {
                    logger.debug("Generating bean factory id from names: " + names);
                }
                id = UUID.nameUUIDFromBytes(names.getBytes()).toString();
            }

            logger.info("BeanFactory id=" + id);
            ((DefaultListableBeanFactory) beanFactory).setSerializationId(id);

        }
        else {
            logger.warn(
                    "BeanFactory was not a DefaultListableBeanFactory, scoped proxy beans "
                            + "cannot be serialized.");
        }

    }

    public void destroy() {
        this.cache.clear();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    private static class BeanWrapper {
        private Object bean;
        private String name;
        private ObjectFactory<?> objectFactory;

        public BeanWrapper(String name, ObjectFactory<?> objectFactory) {
            this.name = name;
            this.objectFactory = objectFactory;
        }

        public Object getBean() {
            if(this.bean == null) {
                synchronized (this.name) {
                    if (this.bean == null) {
                        this.bean = objectFactory.getObject();
                    }
                }
            }
            return this.bean;
        }
    }
}
