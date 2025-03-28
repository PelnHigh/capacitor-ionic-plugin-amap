
import { Capacitor } from '@capacitor/core';
import { JPush } from 'capacitor-plugin-jpushn';

export const JPushSetup = async () => {
    if (Capacitor.isNativePlatform()) {
        // 推送事件监听
        const receivedEvent = await JPush.addListener(
            'notificationReceived',
            (data:any) => {
                console.log(data);
            },
        );
        // 若不需要监听，移除即可
        receivedEvent.remove();

        JPush.addListener('notificationOpened', (data: any) => {
            console.log(data);
        });

        // 检测是否有通知权限
        JPush.checkPermissions().then(async ({ permission }) => {
            console.log(permission);
            if (permission !== 'granted') {
                // 申请通知权限
                JPush.requestPermissions().then(async (res:any) => {
                    console.log(res.permission);
                    if (res.permission === "granted") {
                        // 初始化极光推送
                        await JPush.startJPush();
                    }
                });
                return;
            }
            // 初始化极光推送
            await JPush.startJPush();
        });
    }
};

export const JPushMethods = async () => {
    // 设置推送别名
    await JPush.setAlias({
        alias: 'alias',
    });

    // getRegistrationID
    const { registrationId } = await JPush.getRegistrationID();
    console.log(registrationId);

    // ......
};