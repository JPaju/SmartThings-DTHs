/**
 *  Osram Lightify RGBW A19/BR30 US version (HA) DTH rev 01/19/2018
 *
 *  by gkl_sf
 *
 *  set default color/level code by ranga
 *
 *  To set default initial (power-on) color/level:
 *  - set your preferred color/level
 *  - wait for few seconds, then tap the Set Default tile
 *  - wait 3-5 minutes for the process to complete (do NOT switch off or change any settings during this time)
 *  - the main (on/off) tile will turn orange with "WAIT" status during this period; if it does not reset after 3-5 minutes, tap the refresh tile 
 *  - after that, you can try switching power off and on to see if the new color/level is set correctly
 *  - may need to upgrade firmware (via ST OTA) for this to work
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License. You may obtain a copy of the License at:
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
 *  on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License
 *  for the specific language governing permissions and limitations under the License.
 *
 *  Parts copyright 2015 SmartThings
 *
**/

metadata {
    definition (name: "Osram RGBW HA", namespace: "JPaju", author: "Jaakko Paju") {

        capability "Actuator"
        capability "Color Control"
        capability "Color Temperature"
        capability "Configuration"
        capability "Polling"
        capability "Refresh"
        capability "Switch"
        capability "Switch Level"
        
        command "setHue360", ["number"]

        command "pulseOn"
        command "pulseOff"
        command "setPulseDuration"
        
        command "blinkOn"
        command "blinkOff"
        command "setBlinkDuration"
        
        command "startLoop"
        command "stopLoop"
        command "setLoopDuration"
        command "setLoopDirection"
	    
	    attribute "colorName", "string"
        attribute "colorMode", "string"

        attribute "pulseActive", "string"
        attribute "pulseDuration", "number"

        attribute "blinkActive", "string"
        attribute "blinkDuration", "number"
        
        attribute "loopActive", "string"
        attribute "loopDirection", "string"
        attribute "loopDuration", "number"
        
        fingerprint profileId: "0104", inClusters: "0000,0003,0004,0005,0006,0008,0300,0B04,FC0F", outClusters: "0019", manufacturer: "OSRAM", model: "LIGHTIFY A19 RGBW", deviceJoinName: "Osram Lightify A19 RGBW"
        fingerprint profileId: "0104", inClusters: "0000,0003,0004,0005,0006,0008,0300,0B04,FC0F", outClusters: "0019", manufacturer: "OSRAM", model: "LIGHTIFY BR RGBW", deviceJoinName: "Osram Lightify LED BR30 RGBW"
    }
    
    tiles(scale: 2) {
        multiAttributeTile(name:"switch", type: "lighting", width: 6, height: 4, canChangeIcon: true) {
        
            tileAttribute ("device.switch", key: "PRIMARY_CONTROL") {
                attributeState "on", label:'${name}', action:"off", icon:"st.lights.philips.hue-single", backgroundColor:"#79b821", nextState:"turningOff"
                attributeState "off", label:'${name}', action:"on", icon:"st.lights.philips.hue-single", backgroundColor:"#ffffff", nextState:"turningOn"
                attributeState "turningOn", label:'Turning...', action:"on", icon:"st.lights.philips.hue-single", backgroundColor:"#ffffff", nextState:"turningOn"
                attributeState "turningOff", label:'Turning...', action:"off", icon:"st.lights.philips.hue-single", backgroundColor:"#79b821", nextState:"turningOff"
                attributeState "wait", label:'Wait...', icon:"st.lights.philips.hue-single", backgroundColor:"#ffa81e"                
            }           
            tileAttribute ("device.level", key: "SLIDER_CONTROL") {
                attributeState "level", action:"setLevel"
            }
            tileAttribute ("device.color", key: "COLOR_CONTROL") {
                attributeState "color", action:"setColor"
            }
        }

        // First row, saturation & color              
        valueTile("saturation", "device.saturation", width: 3, height: 1) {
            state "saturation", label:'Saturation ${currentValue}%'
        }
        valueTile("colorName", "device.colorName", width: 3, height: 1) {
            state "colorName", label:'${currentValue}'  
        }
        
        // Hue
        controlTile("hueSliderControl", "device.hue", "slider", width: 3, height: 1, range: "(0..360)") {
            state "hue", action: "setHue360"
        }
        valueTile("hue", "device.hue", width: 3, height: 1) {
            state "hue", label:'Hue ${currentValue}°',
            backgroundColors:[
                    [value: 0, color: "ff0000"],
                    [value: 8, color: "ff3800"],
                    [value: 21, color: "ff6700"],
                    [value: 27, color: "ffbf00"],
                    [value: 41, color: "ffff00"],
                    [value: 55, color: "dfff00"],
                    [value: 95, color: "00ff00"],
                    [value: 125, color: "00ff6f"],
                    [value: 137, color: "00ffff"],
                    [value: 165, color: "007fff"],
                    [value: 206, color: "0000ff"],
                    [value: 246, color: "8f00ff"],
                    [value: 276, color: "ff00ff"],
                    [value: 316, color: "ff007f"],
                    [value: 341, color: "ffc0cb"],
                    [value: 351, color: "dc143c"],
                    [value: 357, color: "ff0000"],
                    [value: 360, color: "ff0000"]                    
		    ]               
        }

        // Color temperature
        controlTile("colorTempSliderControl", "device.colorTemperature", "slider", width: 3, height: 1, range:"(2700..6500)") {
            state "colorTemp", action:"color temperature.setColorTemperature"
        }
        valueTile("colorTemp", "device.colorTemperature", width: 3, height: 1) {
            state "colorTemp", label: 'Temp ${currentValue} K'           
        }

        // Pulse
        controlTile("pulseDurationSlider", "device.pulseDuration", "slider", width: 4, height: 2, range:"(1..10)", inactiveLabel: false) {
            state "pulseDuration", action: "setPulseDuration"
        }    
        standardTile("pulse", "device.pulse", decoration: "flat", width: 2, height: 2) {
            state "off", label:'Pulse', action: "pulseOn", icon: "st.Lighting.light11", backgroundColor:"#ffffff"
            state "on", label:'Pulse', action: "pulseOff", icon: "st.Lighting.light11", backgroundColor:"#dcdcdc"
        }
        
        // Blink
        controlTile("blinkDurationSlider", "device.blinkDuration", "slider", width: 4, height: 2, range:"(100..2000)", inactiveLabel: false) {
            state "blinkDuration", action: "setBlinkDuration"
        }    
        standardTile("blink", "device.blink", decoration: "flat", width: 2, height: 2) {
            state "off", label:'Blink', action: "blinkOn", icon: "st.Lighting.light11", backgroundColor:"#ffffff"
            state "on", label:'Blink', action: "blinkOff", icon: "st.Lighting.light11", backgroundColor:"#dcdcdc"
        }        

        // Color Loop
        controlTile("loopDurationSlider", "device.loopDuration", "slider", width: 2, height: 2, range:"(1..20)", inactiveLabel: false) {
            state "loopDuration", action: "setLoopDuration"
        }    
        standardTile("loopDirection", "device.loopDirection", height: 2, width: 2, inactiveLabel: false, decoration: "flat") {
        	state "default", label: '${currentValue}', action: "setDirection"
        }
        standardTile("loopActive", "device.loopActive", decoration: "flat", width: 2, height: 2) {
            state "off", label:'Color Loop', action: "startLoop", icon: "st.Kids.kids2", backgroundColor:"#ffffff"
            state "on", label:'Color Loop', action: "stopLoop", icon: "st.Kids.kids2", backgroundColor:"#dcdcdc"
        }
        
        main(["switch"])
        details(["switch", "saturation", "colorName", "hueSliderControl", "hue", "colorTempSliderControl", "colorTemp",
            "pulseDurationSlider","pulse", "blinkDurationSlider", "blink", "loopDurationSlider", "loopDirection", "loopActive"])
    }
}

private getON_OFF_CLUSTER() { 6 }
private getLEVEL_CONTROL_CLUSTER() { 8 }
private getCOLOR_CONTROL_CLUSTER() { 0x0300 }

private getHUE_COMMAND() { 0 }
private getSATURATION_COMMAND() { 3 }
private getHUE_SATURATION_COMMAND() { 6 }

private getATTRIBUTE_HUE() { 0 }
private getATTRIBUTE_SATURATION() { 1 }
private getATTRIBUTE_COLOR_TEMPERATURE() { 7 }
private getATTRIBUTE_COLOR_MODE() { 8 }

private getDEFAULT_LEVEL_TRANSITION() {"2800"} //4 secs (little endian)
private getDEFAULT_COLOR_TRANSITION() {"1400"} //2 secs (little endian)
private getDEFAULT_PULSE_DURATION() {"2800"} //4 secs (little endian)
private getDEFAULT_LOOP_RATE() {"05"} //5 steps per sec


def parse(String description) {
    
    if (device.currentValue("loopActive") == "on") return
    
    def result = zigbee.getEvent(description)
    def cmds = []
    
    if (result) {
        cmds << createEvent(result)
        
        log.debug("state.pulseDuration: ${state?.pulseDuration}")

        // Pulse shit
        if (device.currentValue("pulse") == "on" && result.name == "level") {
            if (!state.pulseDuration) state.pulseDuration = DEFAULT_PULSE_DURATION
            if (result.value == 5) cmds << new physicalgraph.device.HubAction("st cmd 0x${device.deviceNetworkId} ${endpointId} 8 4 {fb ${state.pulseDuration}}")
            else if (result.value == 99) cmds << new physicalgraph.device.HubAction("st cmd 0x${device.deviceNetworkId} ${endpointId} 8 4 {0d ${state.pulseDuration}}")            
        }
        else if (result.name == "colorTemperature") {
            if (device.currentValue("colorMode") == "W") {
                def tempName = getTempName(result.value)
                cmds << createEvent(name: "colorName", value: tempName, displayed: false)
            }    
        }
    } else {
        def zigbeeMap = zigbee.parseDescriptionAsMap(description)
        if (zigbeeMap?.clusterInt == COLOR_CONTROL_CLUSTER && device.currentValue("switch") == "on") {        
            if (zigbeeMap.attrInt == ATTRIBUTE_HUE) {
                def hueValue = Math.round(zigbee.convertHexToInt(zigbeeMap.value) / 254 * 360)
                def colorName = getColorName(hueValue)
                cmds << createEvent(name: "hue", value: hueValue, displayed: false)
                cmds << createEvent(name: "colorName", value: colorName, displayed: false)
            }            
            else if (zigbeeMap.attrInt == ATTRIBUTE_SATURATION) {
                def saturationValue = Math.round(zigbee.convertHexToInt(zigbeeMap.value) / 254 * 100)
                cmds << createEvent(name: "saturation", value: saturationValue, displayed: false)
            }
            else if (zigbeeMap.attrInt == ATTRIBUTE_COLOR_MODE) {
                if (zigbeeMap.value == "00") {
                    cmds << createEvent(name: "colorMode", value: "RGB", displayed: false)
                }
                else if (zigbeeMap.value == "02") {
                    cmds << createEvent(name: "colorMode", value: "W", displayed: false)
                }
            }               
        } else if (zigbeeMap?.clusterInt == 0x8021) {
            log.debug "*** received Configure Reporting response: ${zigbeeMap.data}"
        } else {
            log.debug "*** unparsed response: ${zigbeeMap}"
        }
    }
    
    return cmds
}

// def updated() {

//     if (state.updatedTime) {
//         if ((state.updatedTime + 5000) > now()) return null
//     }
//     state.updatedTime = now()

//     log.debug "--- Updated with: ${settings}"

//     String switchTransition
//     if (settings.switchTransition) {
//         switchTransition = hex((settings.switchTransition * 10),4) //OnOffTransitionTime in 1/10th sec (big endian)
//     }
//     else {
//         switchTransition = "0014" //2 seconds (big endian)
//     }    
    
//     if (settings.levelTransition) {
//         state.levelTransition = swapEndianHex(hex((settings.levelTransition * 10),4))
//     }
//     else {
//         state.levelTransition = "2800" //4 seconds
//     }    
    
//     if (settings.colorTransition) {
//         state.colorTransition = swapEndianHex(hex((settings.colorTransition * 10),4))
//     }
//     else {
//         state.colorTransition = "1400" //2 seconds
//     }

//     if (settings.pulseDuration) {
//         state.pulseDuration = swapEndianHex(hex((settings.pulseDuration * 10),4))
//     }
//     else {
//         state.pulseDuration = "2800" //4 seconds
//     }    
    
//     if (settings.loopRate) {
//         state.loopRate = hex((settings.loopRate),2)
//     }
//     else {
//         state.loopRate = "05"
//     }
    
//     return new physicalgraph.device.HubAction("st wattr 0x${device.deviceNetworkId} ${endpointId} 8 0x0010 0x21 {${switchTransition}}")  // on/off dim duration  
// }


def refresh() {
    [
        "st rattr 0x${device.deviceNetworkId} ${endpointId} 6 0", "delay 500", //on-off
        "st rattr 0x${device.deviceNetworkId} ${endpointId} 8 0", "delay 500", //level
        "st rattr 0x${device.deviceNetworkId} ${endpointId} 0x0300 0", "delay 500", //hue
        "st rattr 0x${device.deviceNetworkId} ${endpointId} 0x0300 1", "delay 500", //sat
        "st rattr 0x${device.deviceNetworkId} ${endpointId} 0x0300 7", "delay 500", //color temp
        "st rattr 0x${device.deviceNetworkId} ${endpointId} 0x0300 8" //color mode
    ]
}

def configure() {    
    zigbee.onOffConfig() +
    zigbee.levelConfig() +
    zigbee.colorTemperatureConfig() +
	[        
        //hue
        "zcl global send-me-a-report 0x0300 0 0x20 1 3600 {01}", "delay 500",
        "send 0x${device.deviceNetworkId} ${endpointId} 1", "delay 1000",

        //saturation
        "zcl global send-me-a-report 0x0300 1 0x20 1 3600 {01}", "delay 500",
        "send 0x${device.deviceNetworkId} ${endpointId} 1", "delay 1500",

        //color mode
        "zcl global send-me-a-report 0x0300 8 0x30 1 3600 {}", "delay 500",
        "send 0x${device.deviceNetworkId} ${endpointId} 1", "delay 1500",        
        
        "zdo bind 0x${device.deviceNetworkId} ${endpointId} 1 0x0300 {${device.zigbeeId}} {}", "delay 500"
	] +
    zigbee.writeAttribute(LEVEL_CONTROL_CLUSTER, 0x0010, 0x21, "0014") //OnOffTransitionTime in 1/10th sec, set to 2 sec, note big endian
}

def on() {
    zigbee.on()
}

def off() {
    pulseOff()
    zigbee.off()
}

def setColorTemperature(value) {
    def cmds = []
    cmds << sendEvent(name: "colorMode", value: "W", displayed: false)    
    cmds << zigbee.setColorTemperature(value)
    cmds
}

def setLevel(value, duration = settings.levelTransition) { //duration in seconds
	if (value == 0) off()
    else zigbee.setLevel(value,duration)
}

def setColor(value) {
    if (!state.colorTransition) state.colorTransition = DEFAULT_COLOR_TRANSITION
    def cmds = []
    if (device.currentValue("switch") == "off") {
        cmds << zigbee.on()
        cmds << "delay 500"
    }
    def scaledHueValue = zigbee.convertToHexString(Math.round(value.hue * 0xfe / 100), 2)
    def scaledSatValue = zigbee.convertToHexString(Math.round(value.saturation * 0xfe / 100), 2)
    cmds << sendEvent(name: "saturation", value: value.saturation, displayed: false)
    cmds << sendEvent(name: "colorMode", value: "RGB", displayed: false)
    cmds << zigbee.command(COLOR_CONTROL_CLUSTER, HUE_SATURATION_COMMAND, scaledHueValue, scaledSatValue, state.colorTransition)
    cmds    
}

def setHue(value) { //accepts hue values 0-100, doesn't change saturation
    def cmds = []
    if (!state.colorTransition) state.colorTransition = DEFAULT_COLOR_TRANSITION    
    def scaledHueValue = zigbee.convertToHexString(Math.round(value * 0xfe / 100), 2)
    cmds << sendEvent(name: "colorMode", value: "RGB", displayed: false)
    cmds << zigbee.command(COLOR_CONTROL_CLUSTER, HUE_COMMAND, scaledHueValue, "00", state.colorTransition)
    cmds
}

def setHue360(value) { //accepts hue values 0-360 and sets saturation to 100%
    if (!state.colorTransition) state.colorTransition = DEFAULT_COLOR_TRANSITION    
    def scaledHueValue = zigbee.convertToHexString(Math.round(value * 0xfe / 360), 2)
    def cmds = []
    cmds << sendEvent(name: "saturation", value: 100, displayed: false)
    cmds << sendEvent(name: "colorMode", value: "RGB", displayed: false)
    cmds << zigbee.command(COLOR_CONTROL_CLUSTER, HUE_SATURATION_COMMAND, scaledHueValue, "fe", state.colorTransition)
    cmds
}

def setSaturation(value) {
    if (!state.colorTransition) state.colorTransition = DEFAULT_COLOR_TRANSITION    
    def scaledSatValue = zigbee.convertToHexString(Math.round(value * 0xfe / 100), 2)
    def cmds = []
    cmds << sendEvent(name: "saturation", value: value, displayed: false)
    cmds << sendEvent(name: "colorMode", value: "RGB", displayed: false)
    cmds << zigbee.command(COLOR_CONTROL_CLUSTER, SATURATION_COMMAND, scaledSatValue, state.colorTransition)
    cmds
}

def setPulseDuration(value) {
    log.debug("Setting pulseDuration to: ${value}")
    log.debug("Oma metodi: ${convertNumToHexString(value)}")
    log.debug("Toinen metodi: ${zigbee.convertToHexString(value)}")

	state.pulseDuration = convertNumToHexString(value)

    def cmds = []
	cmds << sendEvent(name:"pulseDuration", value: value)
    cmds
}

def setBlinkDuration(value) {

}

def startLoop(Map params) {
	log.trace "Params: ${params}"
    
	// direction either increments or decrements the hue value: "Up" will increment, "Down" will decrement
    def direction = (device.currentValue("loopDirection") != null ? (device.currentValue("loopDirection") == "Down" ? "00" : "01") : "00")
	    if (params?.direction != null) {
		direction = (params.direction == "Down" ? "00" : "01")
		sendEvent(name: "loopDirection", value: params.direction )
	}

    log.trace "Direction: ${direction}"
		
	// time parameter is the time in seconds for a full loop
	def cycle = (device.currentValue("loopDuration") != null ? device.currentValue("loopDuration") : 2)
	    if (params?.time != null) {
		cycle = params.time
		if (cycle >= 1 && cycle <= 60) {sendEvent(name:"loopDuration", value: cycle)}
	}
    log.trace "Cycle: ${cycle}"

    def finTime = swapEndianHex(hex(cycle, 4))
	log.trace "finTime: ${finTime}"

	def cmds = []
	cmds << "st cmd 0x${device.deviceNetworkId} ${endpointId} 6 1 {}"
    cmds << "delay 200"

	// sendEvent(name: "switchColor", value: "Color Loop", displayed: false)
    sendEvent(name: "loopActive", value: "on")
    	
	if (params?.hue != null) {  
		
		// start hue was specified, so convert to enhanced hue and start loop from there
		def sHue = Math.min(Math.round(params.hue * 255 / 100), 255)
		finHue = swapEndianHex(hex(sHue, 4))
		log.debug "activating color loop from specified hue"
		cmds << "st cmd 0x${device.deviceNetworkId} ${endpointId} 0x300 0x44 {0F 01 ${direction} ${finTime} ${sHue}}"
        
	}
	else {
		       
        // start hue was not specified, so start loop from current hue updating direction and time
		log.debug "activating color loop from current hue"
		cmds << "st cmd 0x${device.deviceNetworkId} ${endpointId} 0x300 0x44 {07 02 ${direction} ${finTime} 0000}"
		
	}
	cmds
}

def stopLoop() {
	
	log.debug "deactivating color loop"
	def cmds = [
		"st cmd 0x${device.deviceNetworkId} ${endpointId} 0x300 0x44 {01 00 00 0000 0000}", "delay 200",
        "st rattr 0x${device.deviceNetworkId} ${endpointId} 0x0300 0", "delay 200",
		"st rattr 0x${device.deviceNetworkId} ${endpointId} 0x0300 1", "delay 200",
        "st rattr 0x${device.deviceNetworkId} ${endpointId} 0x0300 7","delay 200",
        "st rattr 0x${device.deviceNetworkId} ${endpointId} 0x0300 8", "delay 200",
		"st rattr 0x${device.deviceNetworkId} ${endpointId} 8 0"
        ]
	sendEvent(name: "loopActive", value: "off")
	
	cmds
	
}

def setLoopDuration(value) {

    log.trace "Duration value: ${value}"

    sendEvent(name:"loopDuration", value: value)
	if (device.currentValue("loopActive") == "on") {
		def finTime = swapEndianHex(hex(value, 4))
        log.trace "Setting new loop duration to finTime: ${finTime}"
		"st cmd 0x${device.deviceNetworkId} ${endpointId} 0x300 0x44 {04 01 00 ${finTime} 0000}"
	}
}

def setLoopDirection() {
	def direction = (device.currentValue("loopDirection") == "Down" ? "Up" : "Down")
    sendEvent(name: "loopDirection", value: direction)
	if (device.currentValue("loopActive") == "on") {
		def dirHex = (direction == "Down" ? "00" : "01")
        		"st cmd 0x${device.deviceNetworkId} ${endpointId} 0x300 0x44 {02 01 ${dirHex} 0000 0000}"
	}
}

// def loopOn() {
//     if (!state.loopRate) state.loopRate = DEFAULT_LOOP_RATE    
//     def cmds = []
//     cmds << zigbee.command(COLOR_CONTROL_CLUSTER, SATURATION_COMMAND, "fe", "1400") //set saturation to 100% over 2 sec    
//     cmds << sendEvent(name: "colorLoop", value: "on", descriptionText: "Color Loop started", displayed: true, isChange: true)
//     cmds << sendEvent(name: "colorMode", value: "RGB", displayed: false)
//     cmds << zigbee.command(COLOR_CONTROL_CLUSTER, 0x01, "01", state.loopRate) //move hue command is 0x01, up is "01", rate is steps per sec
//     cmds
// }

// def loopOff() {
//     def cmds = []
//     cmds << sendEvent(name: "colorLoop", value: "off", descriptionText: "Color Loop stopped", displayed: true, isChange: true)
//     cmds << zigbee.command(COLOR_CONTROL_CLUSTER, 0x01, "00") //move hue command is 0x01, stop is "00"
//     cmds
// }

def pulseOn() {
    def cmds = []
    cmds << sendEvent(name: "pulse", value: "on", descriptionText: "Pulse mode set to On", displayed: true, isChange: true)
    cmds << zigbee.setLevel(95,0) //in case the level is already 99, since level needs to change to initiate the pulse cycling
    cmds << "delay 100"
    cmds << zigbee.setLevel(99,0)
    cmds
}

def pulseOff() {
    sendEvent(name: "pulse", value: "off", descriptionText: "Pulse mode set to Off", displayed: true, isChange: true)
}

def blinkOn() {
    def cmds = []
    cmds << sendEvent(name: "blink", value: "on", descriptionText: "Blink mode set to On", displayed: true, isChange: true)    
    cmds << zigbee.command(3, 0x00, "100e") //payload is time in secs to continue blinking (set to 3600 secs)
    cmds
}

def blinkOff() {
    def cmds = []
    cmds << sendEvent(name: "blink", value: "off", descriptionText: "Blink mode set to Off", displayed: true, isChange: true)  
    cmds << zigbee.command(3, 0x00, "0000")
    cmds
}

private getEndpointId() {
	new BigInteger(device.endpointId, 16).toString()
}

private hex(value, width=2) {
	def result = new BigInteger(Math.round(value).toString()).toString(16)
	while (result.size() < width) {
		result = "0" + result
	}
	return result
}

private String convertNumToHexString(num) {
    swapEndianHex(hex((num * 10),4))
}

private String swapEndianHex(String hex) {
	reverseArray(hex.decodeHex()).encodeHex()
}

private byte[] reverseArray(byte[] array) {
    byte tmp;
    tmp = array[1];
    array[1] = array[0];
    array[0] = tmp;
    return array
}

private getTempName(value) {
    String tempName
    if (value < 3000) tempName = "Incandescent"
    else if (value < 3300) tempName = "Halogen"
    else if (value < 5000) tempName = "Cool White"
    else if (value <= 6500) tempName = "Daylight"
    else tempName = "White Light Mode"
    return tempName
}

//color name for saturation 100%
private getColorName(hueValue) {
    String colorName
    if (hueValue >= 0 && hueValue <= 7) colorName = "Red"
    else if (hueValue >= 8 && hueValue <= 20) colorName = "Red-Orange"
    else if (hueValue >= 21 && hueValue <= 26) colorName = "Orange"
    else if (hueValue >= 27 && hueValue <= 40) colorName = "Orange-Yellow"
    else if (hueValue >= 41 && hueValue <= 54) colorName = "Yellow"
    else if (hueValue >= 55 && hueValue <= 94) colorName = "Yellow-Green"
    else if (hueValue >= 95 && hueValue <= 124) colorName = "Green"
    else if (hueValue >= 125 && hueValue <= 136) colorName = "Green-Cyan"
    else if (hueValue >= 137 && hueValue <= 164) colorName = "Cyan"
    else if (hueValue >= 165 && hueValue <= 205) colorName = "Cyan-Blue"
    else if (hueValue >= 206 && hueValue <= 243) colorName = "Blue"
    else if (hueValue >= 244 && hueValue <= 272) colorName = "Blue-Magenta"
    else if (hueValue >= 273 && hueValue <= 320) colorName = "Magenta"
    else if (hueValue >= 321 && hueValue <= 349) colorName = "Magenta-Pink"
    else if (hueValue == 350) colorName = "Pink"
    else if (hueValue >= 351 && hueValue <= 356) colorName = "Pink-Red"
    else if (hueValue >= 357 && hueValue <= 360) colorName = "Red"
    else colorName = "Color Mode"
    return colorName
}
