package ru.geekbrains.stargame.pool;


import com.badlogic.gdx.audio.Sound;

import ru.geekbrains.stargame.base.SpritesPool;
import ru.geekbrains.stargame.sprites.Enemy;
import ru.geekbrains.stargame.sprites.MainShip;

public class EnemyPool extends SpritesPool<Enemy> {

    private BulletPool bulletPool;
    private Sound shootSound;
    private MainShip mainShip;
    private Sound explosionSound;
    private ExplosionPool explosionPool;

    public EnemyPool(
            BulletPool bulletPool,
            Sound shootSound,
            MainShip mainShip,
            Sound explosionSound,
            ExplosionPool explosionPool
    ) {
        this.bulletPool = bulletPool;
        this.shootSound = shootSound;
        this.mainShip = mainShip;
        this.explosionSound = explosionSound;
        this.explosionPool = explosionPool;
    }

    @Override
    protected Enemy newObject() {
        return new Enemy(bulletPool, shootSound, mainShip, explosionSound, explosionPool);
    }
}
