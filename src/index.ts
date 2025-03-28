import { registerPlugin } from '@capacitor/core';

import type { AMapPlugin } from './definitions';

const AMap = registerPlugin<AMapPlugin>('AMap');

export * from './definitions';
export { AMap };
