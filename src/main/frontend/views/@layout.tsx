import { Outlet } from 'react-router';
import {
  AppLayout,
  Icon,
  ProgressBar,
  Scroller,
} from '@vaadin/react-components';
import { Suspense } from 'react';

function Header() {
  return (
    <div className="flex p-m gap-m items-center">
      <Icon icon="vaadin:cubes" className="text-primary icon-l" />
      <span className="font-semibold text-l">Tiendplus</span>
    </div>
  );
}

export default function MainLayout() {
  return (
    <AppLayout>
      <Header />
      <Suspense fallback={<ProgressBar indeterminate={true} className="m-0" />}>
        <Outlet />
      </Suspense>
    </AppLayout>
  );
}
