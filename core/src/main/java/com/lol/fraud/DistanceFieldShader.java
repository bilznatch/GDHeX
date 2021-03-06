package com.lol.fraud;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.MathUtils;

public class DistanceFieldShader extends ShaderProgram {
    DistanceFieldShader () {
        super(Gdx.files.internal("Shaders/font.vert"), Gdx.files.internal("Shaders/font.frag"));
        if (!isCompiled()) {
            throw new RuntimeException("Shader compilation failed:\n" + getLog());
        }
    }

    /** @param smoothing a value between 0 and 1 */
    public void setSmoothing (float smoothing) {
        float delta = 0.5f * MathUtils.clamp(smoothing, 0, 1);
        setUniformf("u_lower", 0.5f - delta);
        setUniformf("u_upper", 0.5f + delta);
    }
}
