package app.locationfac.ressources.animation.loadingdrawable.render;

import android.content.Context;
import android.util.SparseArray;

import java.lang.reflect.Constructor;

import app.locationfac.ressources.animation.loadingdrawable.render.circle.rotate.GearLoadingRenderer;
import app.locationfac.ressources.animation.loadingdrawable.render.circle.rotate.LevelLoadingRenderer;
import app.locationfac.ressources.animation.loadingdrawable.render.circle.rotate.MaterialLoadingRenderer;
import app.locationfac.ressources.animation.loadingdrawable.render.circle.rotate.WhorlLoadingRenderer;

public final class LoadingRendererFactory {
    private static final SparseArray<Class<? extends LoadingRenderer>> LOADING_RENDERERS = new SparseArray<>();

    static {
        //circle rotate
        LOADING_RENDERERS.put(0, MaterialLoadingRenderer.class);
        LOADING_RENDERERS.put(1, LevelLoadingRenderer.class);
        LOADING_RENDERERS.put(2, WhorlLoadingRenderer.class);
        LOADING_RENDERERS.put(3, GearLoadingRenderer.class);
    }

    private LoadingRendererFactory() {
    }

    public static LoadingRenderer createLoadingRenderer(Context context, int loadingRendererId) throws Exception {
        Class<?> loadingRendererClazz = LOADING_RENDERERS.get(loadingRendererId);
        Constructor<?>[] constructors = loadingRendererClazz.getDeclaredConstructors();
        for (Constructor<?> constructor : constructors) {
            Class<?>[] parameterTypes = constructor.getParameterTypes();
            if (parameterTypes != null
                    && parameterTypes.length == 1
                    && parameterTypes[0].equals(Context.class)) {
                constructor.setAccessible(true);
                return (LoadingRenderer) constructor.newInstance(context);
            }
        }

        throw new InstantiationException();
    }
}
