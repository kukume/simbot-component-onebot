/*
 * Copyright (c) 2024. ForteScarlet.
 *
 * This file is part of simbot-component-onebot.
 *
 * simbot-component-onebot is free software: you can redistribute it and/or modify it under the terms
 * of the GNU Lesser General Public License as published by the Free Software Foundation,
 * either version 3 of the License, or (at your option) any later version.
 *
 * simbot-component-onebot is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with simbot-component-onebot.
 * If not, see <https://www.gnu.org/licenses/>.
 */

package love.forte.simbot.component.onebot.v11.core.event.message

import love.forte.simbot.component.onebot.v11.core.event.OneBotEvent
import love.forte.simbot.event.MessageEvent

/**
 * OneBot11原始的消息事件结构体定义类型。
 */
public typealias OBSourceMessageEvent = love.forte.simbot.component.onebot.v11.event.message.MessageEvent

/**
 * OneBot组件中的消息相关事件。
 *
 * @author ForteScarlet
 */
public interface OneBotMessageEvent : OneBotEvent, MessageEvent {
    override val sourceEvent: OBSourceMessageEvent
}
