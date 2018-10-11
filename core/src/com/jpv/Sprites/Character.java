package com.jpv.Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.jpv.Level1;
import com.jpv.Scenes.Hud;
import com.jpv.Screens.PlayScreen;

public class Character extends Sprite {
    public enum State {FALLING, JUMPING, STANDING, RUNNING, ATTACKING, DAMAGED, DEAD};
    public State currentState;
    private State prevState;
    private World world;
    public Body b2body;
    private Body arma;
    private Body arma2;
    private Body arma3;
    private Body arma4;
    //private FixtureDef fdefArma;

    private Animation idle;
    private Animation running;
    private Animation attack;
    private Animation dead;
    private Animation damage;
    private TextureRegion falling;
    private TextureRegion jumpAnimation;

    private float stateTimer;
    private float timerVidas;
    private boolean runningRight;
    private boolean attacking;
    private int lifes;
    private boolean damaged;
    private boolean isDead;

    public Character(PlayScreen screen){
        super();
        this.world = screen.getWorld();
        this.currentState = State.STANDING;
        this.prevState = State.STANDING;
        this.runningRight = true;
        lifes = 3;
        damaged = false;
        //attacking = false;
        stateTimer = 0;
        timerVidas = 0;

        Array<TextureRegion> frames = new Array<TextureRegion>();

        //get run animation frames and add them to marioRun Animation
        for(int i= 0; i<13; i++){
            frames.add(new TextureRegion(screen.getAtlas().findRegion("Hank_Run"), i * 175,0,175,175));
        }
        running = new Animation<TextureRegion>(0.20f,frames);
        frames.clear();

        //get damage Animation frames and add them to marioRun Animation
        for(int i= 0; i<4; i++){
            frames.add(new TextureRegion(screen.getAtlas().findRegion("Hank_Damage"), i * 175,0,175,175));
        }
        damage = new Animation<TextureRegion>(0.2f,frames);
        frames.clear();

        //get idle animation frames and add them to marioRun Animation
        for(int i= 0; i<4; i++){
            frames.add(new TextureRegion(screen.getAtlas().findRegion("Hank_Idle"), i * 175,0,175,175));
        }
        idle = new Animation<TextureRegion>(0.7f,frames);
        frames.clear();

        //get attack animation frames and add them to marioRun Animation
        for(int i= 0; i<5; i++){
            frames.add(new TextureRegion(screen.getAtlas().findRegion("Hank_swipe_big"), i * 280,0,280,280));
        }
        attack = new Animation<TextureRegion>(0.3f,frames);
        frames.clear();

        //get jump frame frames and add them to marioRun Animation
        jumpAnimation = new TextureRegion(screen.getAtlas().findRegion("jump_Final"), 175,0,175,175);

        //get fall frame frames and add them to marioRun Animation
        falling = new TextureRegion(screen.getAtlas().findRegion("jump_Final"), 525,0,175,175);

        //get dead animation
        Texture deadd = new Texture("Hank_Dead.png");  //Hank
        TextureRegion[][] temp2 = TextureRegion.split(deadd, 175,175);  //Hank
        TextureRegion[] temp = new TextureRegion[6*1];
        temp[0] = temp2[0][0];
        temp[1] = temp2[0][1];
        temp[2] = temp2[0][2];
        temp[3] = temp2[0][3];
        temp[4] = temp2[0][4];
        temp[5] = temp2[0][5];

        dead = new Animation<TextureRegion>(0.20f,temp);

        defineCharacter();
        setBounds(0,0,175 / Level1.PPM, 175 / Level1.PPM);
    }

    public void update(float dt){
        timerVidas -= dt;
        stateTimer += dt;

        //setPosition(b2body.getPosition().x - getWidth() / 2.3f, b2body.getPosition().y  - getHeight() / 2f); //6.2f
        setPosition(b2body.getPosition().x - getWidth() / 1.8f, b2body.getPosition().y  - getHeight() / 2f);
        setBounds(getX(),getY(),175 / Level1.PPM, 175 / Level1.PPM);
        setRegion((getFrame(dt)));

        if(currentState == State.STANDING && getFrame(dt).isFlipX()){
            setPosition(b2body.getPosition().x - getWidth() / 1.8f, b2body.getPosition().y  - getHeight() / 2f); //6.2f
            setBounds(getX(),getY(),175 / Level1.PPM, 175 / Level1.PPM);
        }else if(currentState == State.ATTACKING) {
            if(attack.getKeyFrameIndex(stateTimer) == 0){
                redefineArma(new Vector2(-75 , 75),
                        new Vector2(-20 , 75),
                         new Vector2(-75, 135),
                        new Vector2(-20 , 135));
            }
            if(attack.getKeyFrameIndex(stateTimer) == 1){
                b2body.destroyFixture(b2body.getFixtureList().get(1));
                redefineArma(new Vector2(50 , 95),
                        new Vector2(0 , 95),
                        new Vector2(50, 155),
                        new Vector2(0 , 155));
            }if(attack.getKeyFrameIndex(stateTimer) == 2){
                b2body.destroyFixture(b2body.getFixtureList().get(1));
                redefineArma(new Vector2(140 , 25),
                        new Vector2(90 , 25),
                        new Vector2(140, 85),
                        new Vector2(90 , 85));
            }if(attack.getKeyFrameIndex(stateTimer) == 3) {
                b2body.destroyFixture(b2body.getFixtureList().get(1));
                redefineArma(new Vector2(110, -85),
                        new Vector2(70, -85),
                        new Vector2(110, -35),
                        new Vector2(70, -35));

            }if(attack.getKeyFrameIndex(stateTimer) == 4){
                for(int i = 1; i < b2body.getFixtureList().size; i++)
                    b2body.destroyFixture(b2body.getFixtureList().get(i));
            }
            if(getFrame(dt).isFlipX()) {
                setPosition(b2body.getPosition().x - getWidth() / 1f, b2body.getPosition().y - getHeight() / 2f); //6.2f
                setBounds(getX(), getY(), 240 / Level1.PPM, 240 / Level1.PPM);
            }else{
                setPosition(b2body.getPosition().x - getWidth() / 2f, b2body.getPosition().y - getHeight() / 2f); //6.2f
                setBounds(getX(), getY(), 240 / Level1.PPM, 240 / Level1.PPM);
                //setBounds(getX(), getY(), 240 / Level1.PPM, 175 / Level1.PPM); Posible solucion pero aplasta a Hank y lo hace ver mal
            }
        }else if(currentState == State.DAMAGED){
            b2body.applyLinearImpulse(new Vector2(-0.1f,0),b2body.getWorldCenter(),true);
        }
    }

    private TextureRegion getFrame(float dt) {
        currentState = getState();

        TextureRegion region;
        switch (currentState) {
            case DEAD:
                region = (TextureRegion) dead.getKeyFrame(stateTimer);
                break;
            case ATTACKING:
                region = (TextureRegion) attack.getKeyFrame(stateTimer);
                if (attack.isAnimationFinished(stateTimer))
                    attacking = false;
                break;
            case DAMAGED:
                region = (TextureRegion) damage.getKeyFrame(stateTimer);
                if (damage.isAnimationFinished(stateTimer))
                    damaged = false;
                break;
            case JUMPING:
                region = jumpAnimation;
                break;
            case RUNNING:
                region = (TextureRegion) running.getKeyFrame(stateTimer, true);
                break;
            case FALLING:
                region = falling;
                break;
            case STANDING:
            default:
                region = (TextureRegion) idle.getKeyFrame(stateTimer, true);
                break;
        }
        if ((b2body.getLinearVelocity().x < 0 || !runningRight) && !region.isFlipX()) {
            region.flip(true, false);
            runningRight = false;
        } else if ((b2body.getLinearVelocity().x > 0 || runningRight) && region.isFlipX()) {
            region.flip(true, false);
            runningRight = true;
        }
        stateTimer = currentState == prevState ? stateTimer + dt : 0;
        prevState = currentState;
        return region;
    }

    private State getState() {
        if (b2body.getLinearVelocity().y > 0 && currentState != State.DAMAGED && !isDead())
            return State.JUMPING;
        else if (b2body.getLinearVelocity().y < 0 || (b2body.getLinearVelocity().y < 0 && currentState == State.JUMPING)
                && currentState != State.DAMAGED && !isDead())
            return State.FALLING;
        else if (b2body.getLinearVelocity().x != 0 && currentState != State.DAMAGED && !isDead())
            return State.RUNNING;
            //aqui empieza la modificacion para el swing completo
        else if (Gdx.input.isKeyPressed(Input.Keys.Z) && !isDead()) {
            attacking = true;
            return State.ATTACKING;
        } else if (attacking)
            return State.ATTACKING;
            //aqui termina la modificacion para el swing
        else if (damaged && !isDead()){ //timerVidas <= 0
            //timerVidas = 1.3f;
            return State.DAMAGED;
        }
        else if(currentState != State.DAMAGED && isDead())
            return State.DEAD;
        else
            return State.STANDING;

    }

    public void hit(){
        if(lifes == 0) {
            isDead = true;

        }else{
            lifes--;
            Hud.updateLifes(true);
            damaged = true;
        }
    }

    private boolean isDead(){
        return isDead;
    }

    public float getStateTimer(){
        return stateTimer;
    }

    private void defineCharacter() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(650 / Level1.PPM ,240 / Level1.PPM); //18650
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();
        shape .setAsBox(40/ Level1.PPM, 80 / Level1.PPM);//modificacion para arreglar el shape salido//no funciono
        fdef.filter.categoryBits = Level1.CHARACTER_BIT;
        fdef.filter.maskBits = Level1.GROUND_BIT
                | Level1.PLATAFORM_BIT
                | Level1.OBSTACULE_BIT
                | Level1.ENEMY_BIT
                | Level1.ENEMY_COLLIDER_BIT
                | Level1.ITEM_BIT
                | Level1.CHARACTER_ARMA_BIT;

        fdef.shape = shape;
        b2body.createFixture(fdef).setUserData(this);

        /*EdgeShape head = new EdgeShape();
        head.set(new Vector2(-20 / Level1.PPM, 80 / Level1.PPM), new Vector2(20 / Level1.PPM, 80 / Level1.PPM));
        fdef.shape = head;
        fdef.filter.categoryBits = Level1.CHARACTER_HEAD_BIT;
        fdef.isSensor = true;
        b2body.createFixture(fdef).setUserData(this);
        */

    }

    private void redefineArma(Vector2 v1, Vector2 v2, Vector2 v3, Vector2 v4){
        FixtureDef fdefArma = new FixtureDef();
        PolygonShape shapeArma = new PolygonShape();
        Vector2[] vertice = new Vector2[4];
        vertice[0] = v1.scl(1 / Level1.PPM);
        vertice[1] = v2.scl(1 / Level1.PPM);
        vertice[2] = v3.scl(1 / Level1.PPM);
        vertice[3] = v4.scl(1 / Level1.PPM);
        shapeArma.set(vertice);

        fdefArma.filter.categoryBits = Level1.CHARACTER_ARMA_BIT;
        fdefArma.filter.maskBits = Level1.ENEMY_BIT
                | Level1.ENEMY_COLLIDER_BIT;

        fdefArma.shape = shapeArma;
        fdefArma.isSensor = true;
        b2body.createFixture(fdefArma).setUserData(this);
    }

    private void redefineArma2(Vector2 v1, Vector2 v2, Vector2 v3, Vector2 v4){
        FixtureDef fdefArma = new FixtureDef();
        PolygonShape shapeArma = new PolygonShape();
        Vector2[] vertice = new Vector2[4];
        vertice[0] = v1.scl(1 / Level1.PPM);
        vertice[1] = v2.scl(1 / Level1.PPM);
        vertice[2] = v3.scl(1 / Level1.PPM);
        vertice[3] = v4.scl(1 / Level1.PPM);
        shapeArma.set(vertice);

        fdefArma.filter.categoryBits = Level1.CHARACTER_ARMA_BIT;
        fdefArma.filter.maskBits = Level1.ENEMY_BIT
                | Level1.ENEMY_COLLIDER_BIT;

        fdefArma.shape = shapeArma;
        fdefArma.isSensor = true;
        b2body.createFixture(fdefArma).setUserData(this);
    }


}
