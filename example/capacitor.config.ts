import type { CapacitorConfig } from '@capacitor/cli';

const config: CapacitorConfig = {
  appId: 'io.ionic.starter',
  appName: 'example',
  webDir: 'dist',
  plugins: {
    JPush: {
      appKey: '324f826bf2b1d17603ca0c39',
      channel: 'developer-default',
    },
  }
};

export default config;
