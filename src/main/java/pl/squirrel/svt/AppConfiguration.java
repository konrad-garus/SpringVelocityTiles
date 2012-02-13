package pl.squirrel.svt;

import java.util.List;

import org.apache.tiles.TilesApplicationContext;
import org.apache.tiles.TilesContainer;
import org.apache.tiles.context.ChainedTilesRequestContextFactory;
import org.apache.tiles.context.TilesRequestContextFactory;
import org.apache.tiles.evaluator.AttributeEvaluatorFactory;
import org.apache.tiles.factory.AbstractTilesContainerFactory;
import org.apache.tiles.factory.BasicTilesContainerFactory;
import org.apache.tiles.renderer.AttributeRenderer;
import org.apache.tiles.renderer.impl.BasicRendererFactory;
import org.apache.tiles.startup.DefaultTilesInitializer;
import org.apache.tiles.velocity.context.VelocityTilesRequestContextFactory;
import org.apache.tiles.velocity.renderer.VelocityAttributeRenderer;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.view.tiles2.TilesConfigurer;
import org.springframework.web.servlet.view.velocity.VelocityConfig;
import org.springframework.web.servlet.view.velocity.VelocityConfigurer;
import org.springframework.web.servlet.view.velocity.VelocityViewResolver;

@Configuration
@ComponentScan(basePackages = "pl.squirrel.svt")
public class AppConfiguration implements ApplicationContextAware {
	private ApplicationContext context;

	public void setApplicationContext(ApplicationContext context)
			throws BeansException {
		this.context = context;
	}

	@Bean
	public VelocityConfig velocityConfig() {
		VelocityConfigurer cfg = new VelocityConfigurer();
		cfg.setResourceLoaderPath("/WEB-INF/velocity/");
		cfg.setConfigLocation(context.getResource("/WEB-INF/velocity.properties"));
		return cfg;
	}

	@Bean
	public TilesConfigurer tilesConfigurer(){
		TilesConfigurer cfg = new TilesConfigurer();
		
		cfg.setTilesInitializer(new DefaultTilesInitializer() {
			@Override
			protected AbstractTilesContainerFactory createContainerFactory(
					TilesApplicationContext context) {
				return new BasicTilesContainerFactory() {

					@Override
					protected List<TilesRequestContextFactory> getTilesRequestContextFactoriesToBeChained(
							ChainedTilesRequestContextFactory parent) {
						List<TilesRequestContextFactory> factories = super
								.getTilesRequestContextFactoriesToBeChained(parent);
						registerRequestContextFactory(
								VelocityTilesRequestContextFactory.class
										.getName(),
								factories, parent);
						return factories;
					}

					@Override
					protected AttributeRenderer createTemplateAttributeRenderer(
							BasicRendererFactory rendererFactory,
							TilesApplicationContext applicationContext,
							TilesRequestContextFactory contextFactory,
							TilesContainer container,
							AttributeEvaluatorFactory attributeEvaluatorFactory) {
						VelocityAttributeRenderer var = new VelocityAttributeRenderer();
						var.setApplicationContext(applicationContext);
						var.setRequestContextFactory(contextFactory);
						var.setAttributeEvaluatorFactory(attributeEvaluatorFactory);
						var.commit();
						return var;
					}
				};
			}});
		return cfg;
	}

	@Bean
	public ViewResolver viewRResolver() {
		VelocityViewResolver resolver = new VelocityViewResolver();
		resolver.setToolboxConfigLocation("/WEB-INF/tools.xml");
		resolver.setSuffix(".vm");
		return resolver;
	}
}
