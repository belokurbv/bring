package org.bobocode.impl.sandbox;

import org.bobocode.annotation.Bean;
import org.bobocode.annotation.Inject;

@Bean("comedyActor")
public class BennyHill {
    @Inject("sampleSandbox")
    public ISandbox sandbox;

    @Inject
    ExampleBean exampleBean;

    public void say() {
        sandbox.hello();
        exampleBean.execute();
    }
}
