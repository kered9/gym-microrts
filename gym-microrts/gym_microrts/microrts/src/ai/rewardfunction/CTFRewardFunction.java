/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ai.rewardfunction;

import java.util.List;

import rts.GameState;
import rts.PhysicalGameState;
import rts.TraceEntry;
import rts.UnitAction;
import rts.units.Unit;
import util.Pair;

import ai.rewardfunction.AttackHolderRewardFunction;
import ai.rewardfunction.CloseHolderToBaseRewardFunction;
import ai.rewardfunction.FlagHeldRewardFunction;
import ai.rewardfunction.ProduceWorkerRewardFunction;
import ai.rewardfunction.ResourceGatherRewardFunction;
import ai.rewardfunction.WinLossRewardFunction;

/**
 *
 * @author costa factor design is inpired by
 *         https://cdn.openai.com/dota-2.pdf#page=44
 */
public class CTFRewardFunction extends RewardFunctionInterface {

    public double factorAttack = 0.4;
    public double factorCloseHolderToBaseR = 0.05;
    public double factorHoldFlag = 0.1;
    public double factorProduceWorker = 0.1;
    public double factorResourceGather = 0.1;
    public double factorWinLoss = 5.0;

    public RewardFunctionInterface attackR;
    public RewardFunctionInterface closeHolderToBaseR;
    public RewardFunctionInterface flagR;
    public RewardFunctionInterface produceWorkerR;
    public RewardFunctionInterface resourceGatherR;
    public RewardFunctionInterface winLossR;

    public CTFRewardFunction(double factorAttack, double factorCloseHolderToBaseR, double factorProduceWorker,
            double factorResourceGather, double factorWinLoss, double factorHoldFlag) {
        this.factorAttack = factorAttack;
        this.factorCloseHolderToBaseR = factorCloseHolderToBaseR;
        this.factorProduceWorker = factorProduceWorker;
        this.factorResourceGather = factorResourceGather;
        this.factorWinLoss = factorWinLoss;
        this.factorHoldFlag = factorHoldFlag;

        attackR = new AttackHolderRewardFunction();
        closeHolderToBaseR = new CloseHolderToBaseRewardFunction();
        produceWorkerR = new ProduceWorkerRewardFunction();
        resourceGatherR = new ResourceGatherRewardFunction();
        winLossR = new WinLossRewardFunction();
        flagR = new FlagHeldRewardFunction();
    }

    public void computeReward(int maxplayer, int minplayer, TraceEntry te, GameState afterGs) {
        reward = 0.0;
        done = false;
        attackR.computeReward(maxplayer, minplayer, te, afterGs);
        closeHolderToBaseR.computeReward(maxplayer, minplayer, te, afterGs);
        produceWorkerR.computeReward(maxplayer, minplayer, te, afterGs);
        resourceGatherR.computeReward(maxplayer, minplayer, te, afterGs);
        winLossR.computeReward(maxplayer, minplayer, te, afterGs);
        flagR.computeReward(maxplayer, minplayer, te, afterGs);
        done = winLossR.isDone();

        reward = factorAttack * attackR.getReward() + factorCloseHolderToBaseR * closeHolderToBaseR.getReward()
                + factorProduceWorker * produceWorkerR.getReward() + factorResourceGather * resourceGatherR.getReward()
                + factorWinLoss * winLossR.getReward() + factorHoldFlag * flagR.getReward();
    }

    public double getReward() {
        return reward;
    }

    public boolean isDone() {
        return done;
    }
}
