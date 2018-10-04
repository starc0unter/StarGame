package ru.geekbrains.stargame.base;


import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.ArrayList;
import java.util.List;

public abstract class SpritesPool<T extends Sprite> {

    // список активных объектов
    protected List<T> activeObjects = new ArrayList<T>();
    // список свободных объектов
    protected List<T> freeObjects = new ArrayList<T>();

    protected abstract T newObject();

    public T obtain() {
        T object;
        if (freeObjects.isEmpty()) {
            object = newObject();
        } else {
            object = freeObjects.remove(freeObjects.size() - 1);
        }
        activeObjects.add(object);
        log();
        return object;
    }

    public void updateActiveObjects(float delta) {
        for (int i = 0; i < activeObjects.size(); i++) {
            Sprite sprite = activeObjects.get(i);
            if (!sprite.isDestroyed()) {
                sprite.update(delta);
            }
        }
    }

    public void freeAllDestroyedActiveObjects() {
        for (int i = 0; i < activeObjects.size(); i++) {
            T sprite = activeObjects.get(i);
            if (sprite.isDestroyed()) {
                free(sprite);
                i--;
                sprite.flushDestroy();
            }
        }
    }

    private void free(T object) {
        if (activeObjects.remove(object)) {
            freeObjects.add(object);
            log();
        }
    }

    public void drawActiveObjects(SpriteBatch batch) {
        for (int i = 0; i < activeObjects.size(); i++) {
            Sprite sprite = activeObjects.get(i);
            if (!sprite.isDestroyed()) {
                sprite.draw(batch);
            }
        }
    }

    public List<T> getActiveObjects() {
        return activeObjects;
    }

    public void dispose() {
        activeObjects.clear();
        freeObjects.clear();
    }

    protected void log() {

    }
}
