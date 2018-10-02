package ru.geekbrains.stargame.sprites;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import ru.geekbrains.stargame.base.Ship;
import ru.geekbrains.stargame.math.Rect;
import ru.geekbrains.stargame.pool.BulletPool;
import ru.geekbrains.stargame.pool.ExplosionPool;


public class Enemy extends Ship {

    private MainShip mainShip;
    private final float DEFAULT_APPEAR_SPEED = -0.2f;

    private Vector2 v0 = new Vector2();
    private Vector2 appearV = new Vector2(0, DEFAULT_APPEAR_SPEED);

    private Sound explosionSound;
    private ExplosionPool explosionPool;

    public Enemy(
            BulletPool bulletPool,
            Sound shootSound,
            MainShip mainShip,
            Sound explosionSound,
            ExplosionPool explosionPool
            ) {
        super(bulletPool, shootSound);
        this.mainShip = mainShip;
        this.v.set(v0);
        this.explosionPool = explosionPool;
        this.explosionSound = explosionSound;
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        if (getTop() > worldBounds.getTop()) pos.mulAdd(appearV, delta);
        else { pos.mulAdd(v, delta); }

        reloadTimer += delta;
        if (reloadTimer >= reloadInterval) {
            reloadTimer = 0f;
            shoot();
        }

        if (getBottom() <= worldBounds.getBottom()) {
            destroy();
            Explosion explosion = explosionPool.obtain();
            explosion.set(0.2f, pos);
            explosionSound.play();
        }
    }

    public void set(
            TextureRegion[] regions,
            Vector2 v0,
            TextureRegion bulletRegion,
            float bulletHeight,
            float bulletVY,
            int bulletDamage,
            float reloadInterval,
            float height,
            int hp,
            Rect worldBounds
    ) {
        this.regions = regions;
        this.v0.set(v0);
        this.bulletRegion = bulletRegion;
        this.bulletHeight = bulletHeight;
        this.bulletV.set(0, bulletVY);
        this.bulletDamage = bulletDamage;
        this.reloadInterval = reloadInterval;
        this.hp = hp;
        this.worldBounds = worldBounds;
        setHeightProportion(height);
        reloadTimer = reloadInterval;
        v.set(v0);
    }
}
