package com.jpv.Bugged.Niveles.Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.jpv.Bugged.Niveles.LevelManager;
import com.jpv.Bugged.Niveles.Screens.PlayScreen;


public class Character extends Sprite {
    //region VARIABLES
    public enum State {FALLING, JUMPING, STANDING, RUNNING, ATTACKING, DAMAGED, DEAD, WIN};
    public State currentState;
    private State prevState;
    private World world;
    public PlayScreen screen;
    public Body b2body;

    private Animation idle;
    private Animation running;
    private Animation attack;
    private Animation dead;
    private Animation damage;
    private TextureRegion falling;
    private TextureRegion jumpAnimation;

    private float stateTimer;
    private boolean runningRight;
    public boolean boss;
    private boolean attacking;
    private int lifes;
    public boolean damaged;
    private boolean isDead;
    private boolean firstDam;
    public boolean win;
    //endregion

    //region CODE
    public Character(PlayScreen screen){
        //region INITIALIZE VARIABLE
        super();
        this.screen = screen;
        this.world = screen.getWorld();
        this.currentState = State.STANDING;
        this.prevState = State.STANDING;
        this.runningRight = true;
        lifes = 3;
        damaged = false;
        stateTimer = 0;
        firstDam = true;
        boss=false;
        win = false;
        //endregion
        //region ANIMATION
        Array<TextureRegion> frames = new Array<TextureRegion>();
        //region RUN
        //get run animation frames and add them to marioRun Animation
        for(int i= 0; i<13; i++){
            frames.add(new TextureRegion(screen.getAtlas().findRegion("Hank_Run"), i * 175,0,175,175));
        }
        running = new Animation<TextureRegion>(0.09f,frames);
        frames.clear();
        //endregion
        //region DAMAGE
        //get damage Animation frames and add them to marioRun Animation
        TextureRegion temp;
        for(int i= 0; i<4; i++){
            temp = new TextureRegion(screen.getAtlas().findRegion("Hank_Damage"), i * 175,0,175,175);
            temp.flip(true,false);
            frames.add(temp);
        }
        damage = new Animation<TextureRegion>(0.2f,frames);
        frames.clear();
        //endregion
        //region IDLE
        //get idle animation frames and add them to marioRun Animation
        for(int i= 0; i<4; i++){
            frames.add(new TextureRegion(screen.getAtlas().findRegion("Hank_Idle"), i * 175,0,175,175));
        }
        idle = new Animation<TextureRegion>(0.7f,frames);
        frames.clear();
        //endregion
        //region ATTACK
        //get attack animation frames and add them to marioRun Animation
        for(int i= 0; i<5; i++){
            frames.add(new TextureRegion(screen.getAtlas().findRegion("Hank_Swipe"), i * 280,0,280,280));
        }
        //frames.add(new TextureRegion(new Texture("vacia.png")));
        attack = new Animation<TextureRegion>(0.3f,frames);
        frames.clear();
        //endregion
        //region JUMP
        //get jump frame frames and add them to marioRun Animation
        jumpAnimation = new TextureRegion(screen.getAtlas().findRegion("Hank_Jump"), 0,0,175,175);
        //endregion
        //region FALL
        //get fall frame frames and add them to marioRun Animation
        falling = new TextureRegion(screen.getAtlas().findRegion("Hank_Jump"), 175,0,175,175);
        //endregion
        //region DEAD
        //get dead animation frames and add them to marioRun Animation
        for(int i= 0; i<6; i++){
            frames.add(new TextureRegion(screen.getAtlas().findRegion("Hank_Dead"), i * 175,0,175,175));
        }
        dead = new Animation<TextureRegion>(0.1f,frames);
        //endregion

        //endregion
        defineCharacter();
        setBounds(0,0,175 / LevelManager.PPM, 175 / LevelManager.PPM);
    }

    public void update(float dt){
        //region STANDING REGION AND DEFAULT
        TextureRegion frames = getFrame(dt);
        //if(currentState == State.STANDING || currentState != State.ATTACKING) {
            setPosition(b2body.getPosition().x - getWidth() / 2.3f, b2body.getPosition().y - getHeight() / 2f); //6.2f
            setBounds(getX(), getY(), 175 / LevelManager.PPM, 175 / LevelManager.PPM);
            setRegion(frames);
            if (currentState == State.STANDING && getFrame(dt).isFlipX()) {
                setPosition(b2body.getPosition().x - getWidth() / 1.5f, b2body.getPosition().y - getHeight() / 2f);
            }
        //}
        //endregion

        //region ATTACKING
        if(currentState == State.ATTACKING) {
            setPosition(b2body.getPosition().x - getWidth() / 1.8f, b2body.getPosition().y  - getHeight() / 0.5f);
            if(frames.isFlipX()) {
                setPosition(b2body.getPosition().x - getWidth() / 1f, b2body.getPosition().y - getHeight() / 2); //6.2f
                setBounds(getX(), getY(), 240 / LevelManager.PPM, 240 / LevelManager.PPM);
                setRegion(frames);
            }if(!frames.isFlipX()){
                setPosition(b2body.getPosition().x - getWidth() / 2f, b2body.getPosition().y - getHeight() / 2f); //6.2f
                setBounds(getX(), getY(), 240 / LevelManager.PPM, 240 / LevelManager.PPM);
                setRegion(frames);
            }
            if(!getFrame(dt).isFlipX()){
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
            }else{
                if(attack.getKeyFrameIndex(stateTimer) == 0){
                    redefineArma(new Vector2(75 , 75),
                            new Vector2(20 , 75),
                            new Vector2(75, 135),
                            new Vector2(20 , 135));
                }
                if(attack.getKeyFrameIndex(stateTimer) == 1){
                    b2body.destroyFixture(b2body.getFixtureList().get(1));
                    redefineArma(new Vector2(-50 , 95),
                            new Vector2(0 , 95),
                            new Vector2(-50, 155),
                            new Vector2(0 , 155));
                }if(attack.getKeyFrameIndex(stateTimer) == 2){
                    b2body.destroyFixture(b2body.getFixtureList().get(1));
                    redefineArma(new Vector2(-140 , 25),
                            new Vector2(-90 , 25),
                            new Vector2(-140, 85),
                            new Vector2(-90 , 85));
                }if(attack.getKeyFrameIndex(stateTimer) == 3) {
                    b2body.destroyFixture(b2body.getFixtureList().get(1));
                    redefineArma(new Vector2(-110, -85),
                            new Vector2(-70, -85),
                            new Vector2(-110, -35),
                            new Vector2(-70, -35));
                }if(attack.getKeyFrameIndex(stateTimer) == 4){
                    for(int i = 1; i < b2body.getFixtureList().size; i++)
                        b2body.destroyFixture(b2body.getFixtureList().get(i));
                }
            }
        }
        //endregion
        if(currentState == State.STANDING){
            setPosition(b2body.getPosition().x - getWidth() / 2.3f, b2body.getPosition().y  - getHeight() / 2f); //6.2f
            setBounds(getX(),getY(),175 / LevelManager.PPM, 175 / LevelManager.PPM);
            frames = getFrame(dt);
            setRegion(frames);
            if(currentState == State.STANDING && getFrame(dt).isFlipX()){
                setPosition(b2body.getPosition().x - getWidth() / 1.5f, b2body.getPosition().y  - getHeight() / 2f);
            }
        }
        //region DAMAGED
        else if(currentState == State.DAMAGED) {
            if(!isDead()) {
                if(isFlipX() && firstDam) {
                    b2body.applyLinearImpulse(new Vector2(3f, 0), b2body.getWorldCenter(), true);
                    firstDam = false;
                }else if(!isFlipX() && firstDam){
                    b2body.applyLinearImpulse(new Vector2(-3f, 0), b2body.getWorldCenter(), true);
                    firstDam = false;
                }
            }
        }
        //endregion
        //region No DAMAGE LOGIC
        if(currentState != State.DAMAGED){
            firstDam = true;
        }
        //endregion
        //region DEAD
        if(b2body.getPosition().y <1){
            isDead = true;
        }
        else if(currentState == State.DEAD) {
            /*if (first) {
                for (Enemy enemy : screen.getCreator().getMosquitos())
                    world.destroyBody(enemy.b2body);

                /*Filter filter = new Filter();
                filter.maskBits = Level1.NOTHING_BIT;
                for (Fixture fixture : screen.getCreator().getTheRedBug().b2body.getFixtureList()) {
                    fixture.setFilterData(filter);
                }
                for (Fixture fix : screen.getCreator().getTheRedBug().b2body.getFixtureList()) {
                    if(!fix.equals(b2body.getFixtureList().get(0))) {
                        screen.getCreator().getTheRedBug().b2body.destroyFixture(fix);
                    }
                }
                first = false;
            }
            */
        }
        //endregion
        //region NO ATTACKING LOGIC
        if(currentState != State.ATTACKING){
            for(Fixture fix : b2body.getFixtureList()){
                if(!fix.equals(b2body.getFixtureList().get(0)))
                    b2body.destroyFixture(fix);
            }
        }
        //endregion
    }

    private TextureRegion getFrame(float dt) {
        currentState = getState();

        stateTimer = currentState == prevState ? stateTimer + dt : 0;
        prevState = currentState;


        TextureRegion region;
        switch (currentState) {
            case DEAD:
                region = (TextureRegion) dead.getKeyFrame(stateTimer);
                break;
            case ATTACKING:
                region = (TextureRegion) attack.getKeyFrame(stateTimer);
                if (attack.isAnimationFinished(stateTimer)) {
                    attacking = false;
                    screen.getHud().setBtnAt(false);
                    stateTimer = 0;
                }
                break;
            case DAMAGED:
                region = (TextureRegion) damage.getKeyFrame(stateTimer);
                if (damage.isAnimationFinished(stateTimer)) {
                    damaged = false;
                    attacking = false;
                    firstDam = true;
                }
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
            case WIN:
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

        //stateTimer = currentState == prevState ? stateTimer + dt : 0;

        return region;
    }

    private State getState() {
        if(currentState!=State.WIN){
        if(currentState != State.DAMAGED && isDead()) {
            return State.DEAD;
        }
        else if (damaged && !isDead()){
            return State.DAMAGED;
        }
        else if ((Gdx.input.isKeyPressed(Input.Keys.Z) || screen.getHud().getBtnAt()) && !attacking && !isDead()){
            attacking = true;
            return State.ATTACKING;
        }
        else if (b2body.getLinearVelocity().y > 0 && currentState != State.DAMAGED && !isDead()) {
            if (!attacking) {
                return State.JUMPING;
            }
            else {
                return State.ATTACKING;
            }
        }
        else if (b2body.getLinearVelocity().y < 0 && !isDead()){
            if(!attacking) {
                return State.FALLING;
            }
            else {
                return State.ATTACKING;
            }
        }
        else if (b2body.getLinearVelocity().x != 0 && currentState != State.DAMAGED && !isDead()){
            if(attacking) {
                return State.ATTACKING;
            }
            else if(damaged){
                return State.DAMAGED;
            }
            else {
                return State.RUNNING;
            }
        }
        //aqui empieza la modificacion para el swing completo

        else if (attacking) {
            return State.ATTACKING;
        }else if(damaged){
            return State.DAMAGED;
        }

        //aqui termina la modificacion para el swing
        else if(win){
            return State.WIN;
        }
        else{
            return State.STANDING;
        }
        }
        else
            return State.WIN;
    }

    public void hit() {
        if(currentState != State.DAMAGED) {
            if(currentState != State.ATTACKING) {
                if (lifes <= 1) {
                    screen.getHud().updateLifes(-1);
                    isDead = true;

                } else {
                    lifes--;
                    screen.getHud().updateLifes(-1);
                    damaged = true;
                }
            }
        }
    }

    public void kill() {
        screen.getHud().updateLifes(1);
        isDead = true;
    }

    //region GETTERS
    private boolean isDead(){
        return isDead;
    }
    public float getStateTimer(){
        return stateTimer;
    }
    public void setLife(){
        this.lifes += 1;
    }
    //endregion

    private void defineCharacter() {
        BodyDef bdef = new BodyDef();//650
        bdef.position.set(11350 / LevelManager.PPM ,240 / LevelManager.PPM); //11350
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();
        shape .setAsBox(40/ LevelManager.PPM, 80 / LevelManager.PPM);//modificacion para arreglar el shape salido//no funciono
        fdef.filter.categoryBits = LevelManager.CHARACTER_BIT;
        fdef.filter.maskBits = LevelManager.GROUND_BIT
                | LevelManager.PLATAFORM_BIT
                | LevelManager.OBSTACULE_BIT
                | LevelManager.ENEMY_BIT
                | LevelManager.ENEMY_COLLIDER_BIT
                | LevelManager.ITEM_BIT
                | LevelManager.BOSS_COLLIDER_BIT
                | LevelManager.BOSS_PIES_BIT;

        fdef.shape = shape;
        b2body.createFixture(fdef).setUserData(this);
    }

    private void redefineArma(Vector2 v1, Vector2 v2, Vector2 v3, Vector2 v4){
        FixtureDef fdefArma = new FixtureDef();
        PolygonShape shapeArma = new PolygonShape();
        Vector2[] vertice = new Vector2[4];
        vertice[0] = v1.scl(1 / LevelManager.PPM);
        vertice[1] = v2.scl(1 / LevelManager.PPM);
        vertice[2] = v3.scl(1 / LevelManager.PPM);
        vertice[3] = v4.scl(1 / LevelManager.PPM);
        shapeArma.set(vertice);

        fdefArma.filter.categoryBits = LevelManager.CHARACTER_ARMA_BIT;
        fdefArma.filter.maskBits = LevelManager.ENEMY_BIT
                | LevelManager.ENEMY_COLLIDER_BIT
                | LevelManager.BOSS_PIES_BIT
                | LevelManager.BOSS_COLLIDER_BIT
                | LevelManager.BOSS_BIT;

        fdefArma.shape = shapeArma;
        fdefArma.isSensor = true;
        b2body.createFixture(fdefArma).setUserData(this);
    }

    //endregion


}
