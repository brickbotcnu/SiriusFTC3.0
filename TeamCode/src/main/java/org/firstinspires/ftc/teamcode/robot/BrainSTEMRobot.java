package org.firstinspires.ftc.teamcode.robot;
import android.hardware.HardwareBuffer;

import com.acmerobotics.roadrunner.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.MecanumDrive;

import java.util.Map;
public class BrainSTEMRobot {
    private ElapsedTime elapsedTime = new ElapsedTime();
    private Telemetry telemetry;
    private OpMode opMode;
    public Lift lift;
    public Hopper hopper;
    public Fulcrum fulcrum;
    public Intake intake;
    public MecanumDrive drive;
    private Map stateMap;
    Constants constants = new Constants();

    public BrainSTEMRobot(HardwareMap hardwareMap, Telemetry telemetry, Map stateMap){
        this.telemetry = telemetry;
        this.stateMap =  stateMap;

//        lift = new Lift(hardwareMap, telemetry, stateMap);
        hopper = new Hopper(hardwareMap, telemetry, stateMap);
        intake = new Intake(hardwareMap, telemetry, stateMap, hopper);
//        fulcrum = new Fulcrum(hardwareMap, telemetry, stateMap);
//        drive = new MecanumDrive(hardwareMap, new Pose2d(0,0,0));

        stateMap.put(constants.PIXEL_CYCLE, constants.PIXEL_CYCLE_STATE_NOT_STARTED);
        stateMap.put(constants.PIXEL_CYCLE_INTAKE, constants.PIXEL_CYCLE_STATE_NOT_STARTED);
        stateMap.put(constants.PIXEL_CYCLE_FULCRUM, constants.PIXEL_CYCLE_STATE_NOT_STARTED);

        telemetry.addData("Robot", "is ready");
        telemetry.update();
    }

    public void updateSystems(){
        telemetry.addData("Robot stateMap", stateMap);
        String pixelPickup = (String)(stateMap.get(constants.PIXEL_CYCLE));

        if(pixelPickup.equals(constants.PIXEL_CYCLE_STATE_IN_PROGRESS)){
            pixelPickupFunction();
        } else {
            hopper.setState();
            intake.setState();
//            fulcrum.set_state();
        }
    }

    private void pixelPickupFunction() {
        telemetry.addData("Start intake function", startIntake());
        if (startIntake()) {
            stateMap.put(constants.PIXEL_CYCLE_INTAKE, constants.PIXEL_CYCLE_STATE_IN_PROGRESS);
        }
        setPixelPickupSubsystems();
    }

    private boolean pixelPickupComplete(){
        if(((String)(stateMap.get(constants.PIXEL_CYCLE_INTAKE))).equals(constants.PIXEL_CYCLE_STATE_COMPLETE)){
            return true;
        }
        return false;
    }
    private void setPixelPickupSubsystems(){
        intake.setState();
        hopper.setState();
    }
    private boolean startIntake(){
        String pixelCycleState = (String)(stateMap.get(constants.PIXEL_CYCLE));
        String intakeState = (String) stateMap.get(constants.PIXEL_CYCLE_INTAKE);
        if(pixelCycleState.equalsIgnoreCase(constants.PIXEL_CYCLE_STATE_IN_PROGRESS) && intakeState.equalsIgnoreCase(constants.PIXEL_CYCLE_STATE_NOT_STARTED)){
            return true;
        }
        return false;
    }
}
